package com.mpool.admin.module.bill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.InModel.InBlock;
import com.mpool.admin.exception.AdminException;
import com.mpool.admin.mapperUtils.bill.UserPayBillItemMapperUtils;
import com.mpool.admin.mapperUtils.bill.UserPayBillMapperUtils;
import com.mpool.admin.module.alarm.service.AlarmSettingService;
import com.mpool.admin.module.alarm.service.impl.NotifyServicePoolPay;
import com.mpool.admin.module.bill.mapper.UserPayMapper;
import com.mpool.admin.module.bill.service.UserPayBillService;
import com.mpool.admin.module.log.service.LogAdminOperationService;
import com.mpool.admin.module.system.mapper.SysUserMapper;
import com.mpool.admin.module.system.service.CurrencyService;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.admin.utils.NotifyUtils;
import com.mpool.common.model.account.bill.UserPay;
import com.mpool.common.model.account.bill.UserPayBill;
import com.mpool.common.model.account.bill.UserPayBillItem;
import com.mpool.common.model.admin.SysAlarmSetting;
import com.mpool.common.model.admin.SysUser;
import com.mpool.common.model.log.LogUserOperation;
import com.mpool.common.util.BTCUtil;
import com.mpool.common.util.BillNumberUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;

@Service
public class UserPayBillServiceImpl implements UserPayBillService {

	private static final Logger log = LoggerFactory.getLogger(UserPayBillServiceImpl.class);
	@Autowired
	private UserPayMapper btcUserPayMapper;
	@Autowired
	private UserPayBillMapperUtils btcUserPayBillMapperUtils;
	@Autowired
	private UserPayBillItemMapperUtils btcUserPayBillItemMapperUtils;
	@Autowired
	private NotifyServicePoolPay notifyServicePoolPay;
	@Autowired
	private AlarmSettingService alarmSettingService;

	@Autowired
	private ExecutorService executorService;
	@Autowired
	private SysUserMapper sysUserMapper;
	@Autowired
	private NotifyUtils notifyUtils;
	@Autowired
	private LogAdminOperationService userOperationService;
	@Autowired
	private CurrencyService currencyService;

	@Override
	@Transactional
	public void createBillNumber(Long userId, List<Long> itemIds) {
		String billNumber = BillNumberUtil.getInstance().nextId();
		UserPayBill btcUserPayBill = new UserPayBill();
		btcUserPayBill.setBillNumber(billNumber);
		btcUserPayBill.setCreateBy(userId);
		btcUserPayBill.setCteateAt(new Date());
		btcUserPayBill.setStatus(1);
		btcUserPayBill.setPayCount(itemIds.size());
		int row = btcUserPayBillItemMapperUtils.updateBillNumber(billNumber, itemIds);
		Long allBtc = btcUserPayBillItemMapperUtils.getAllBtcByBillNumber(billNumber);

		if (itemIds.size() != row) {
			throw new AdminException("bill.error", "创建支付单错误");
		}
		btcUserPayBill.setPayAllBtc(allBtc);
		btcUserPayBillMapperUtils.insert(btcUserPayBill);

		//支付后通知管理员
		String adminId = userId+"";
		notifyUtils.createBillNotify(adminId);
		//记录日志
		SysUser sysUser = AdminSecurityUtils.getUser();//获取当前登录的管理员
		executorService.execute(() -> {
				if (sysUser!= null){
					LogUserOperation model = new LogUserOperation();
					model.setLogType(2);//2矿池日志
					model.setUserType("admin");//操作人类型是管理员
					model.setCreatedTime(new Date());
					model.setUserId(sysUser.getUserId());
					model.setContent("管理员["+sysUser.getUsername()+"]-创建支付账单["+billNumber+"]，操作成功");
					userOperationService.insert(model);
			}
		});
	}

	@Override
	public IPage<Map<String, Object>> getBillInfo(IPage<Map<String, Object>> iPage, Map<String, Object> param) {
		IPage<Map<String, Object>> billInfo = btcUserPayBillMapperUtils.getBillInfo(iPage, param);
		billInfo.getRecords().forEach(m -> {
			Object object = m.get("pay_by");
			if (object != null) {
				String usernameByUserId = sysUserMapper.getUsernameByUserId(object.toString());
				m.put("operat", usernameByUserId);
			}
			log.debug("billInfo -> {}", m);
		});
		return billInfo;
	}

	@Override
	@Transactional // 开启事物
	public void pay(String billNumber, String txid) {
		UserPayBill tmp_btcUserPayBill = btcUserPayBillMapperUtils.getBillInfoByNumOrTxid(null, txid);
		if (tmp_btcUserPayBill != null) {
			throw new AdminException("bill.txid.exist", "TXID冲突");
		}

		UserPayBill btcUserPayBill = btcUserPayBillMapperUtils.selectById(billNumber);
		if (btcUserPayBill == null) {
			throw new AdminException("bill.not.found", "账单未找到");
		}
		if (btcUserPayBill.getStatus() == 0) {
			throw new AdminException("status.error", "账单状态已完成");
		}
		UserPayBillItem entity = new UserPayBillItem();
		entity.setBillNumber(billNumber);

		List<UserPayBillItem> selectList = btcUserPayBillItemMapperUtils
				.selectList(new QueryWrapper<UserPayBillItem>(entity));
		if (btcUserPayBill.getPayCount() != selectList.size()) {
			log.error("pay count {} query size {}", btcUserPayBill.getPayCount(), selectList.size());
			throw new AdminException("bill.exception", "账单异常");
		}
		// 支付
		Date date = new Date();
		for (UserPayBillItem btcUserPayBillItem : selectList) {

			UserPay btcUserPay = new UserPay();
			String btcWalletAddress = btcUserPayBillItem.getWalletAddress();
			Long puid = btcUserPayBillItem.getPuid();

			btcUserPay.setWalletAddress(btcWalletAddress);
			btcUserPay.setPuid(puid);

			UserPay one = btcUserPayMapper.selectOne(new QueryWrapper<UserPay>(btcUserPay));

			if (one == null) {
				log.error("user wallet not found ------>puid = {},btcWalletAddress = {}", puid, btcWalletAddress);
				throw new AdminException("user.pay.not.found", "用户钱包未找到!");
			}

			Long payBtc = btcUserPayBillItem.getPayBtc();
			Long totalPaid = one.getTotalPaid();
			Long newbtc = payBtc + totalPaid;
			log.debug("id ={},pay userid = {} ,old btc={},new btc ={} ,", one.getId(), one.getPuid(), totalPaid,
					newbtc);
			one.setTotalPaid(newbtc);
			one.setLastPayAt(date);
			btcUserPayMapper.updateById(one);
		}
		btcUserPayBill.setTxid(txid);
		btcUserPayBill.setStatus(0);
		btcUserPayBill.setPayAt(date);
		Long payBy = AdminSecurityUtils.getUser().getUserId();
		btcUserPayBill.setPayBy(payBy);
		btcUserPayBillMapperUtils.updateById(btcUserPayBill);
		executorService.execute(() -> {
			notify(btcUserPayBill);
		});
	}

	private void notify(UserPayBill userPayBill) {
		List<SysAlarmSetting> alarmSettingNotify = alarmSettingService.getAlarmSettingNotify(currencyService.getCurCurrencyId());

		Long payAllBtc = userPayBill.getPayAllBtc();
		Map<String, Object> notifyModel = new HashMap<>();
		notifyModel.put("value", BigDecimal.valueOf(payAllBtc).divide(BigDecimal.valueOf(100000000)));
		for (SysAlarmSetting sysAlarmSetting : alarmSettingNotify) {
			Long pay = sysAlarmSetting.getPay();
			Long userId = sysAlarmSetting.getUserId();
			if (pay < payAllBtc) {
				notifyServicePoolPay.sendNotify(sysAlarmSetting, userId, notifyModel);
			}

		}

	}

	@Override
	public UserPayBill getBillInfoByNumOrTxid(String bilNum, String txid) {

		UserPayBill userPayBill = btcUserPayBillMapperUtils.getBillInfoByNumOrTxid(bilNum, txid);
		if (userPayBill == null) {
			throw new RuntimeException("输入的信息错误");
		}
		return userPayBill;
	}

	@Override
	public Map<String, Object> getBillInfoByDate(InBlock date, IPage<Map<String, Object>> iPage) {
		Date strTime = date.getStrTime();
		Date endTime = date.getEndTime();
		// 结果封装map返回
		Map<String, Object> map = new HashMap<>();
		// 如果不传时间则查全部
		if (strTime == null && endTime == null) {
			IPage<Map<String, Object>> billInfo = btcUserPayBillMapperUtils.getBillInfo(iPage, null);
			List<Map<String, Object>> userPayBills = billInfo.getRecords();
			Long payAllBtcSum = 0L;
			for (Map<String, Object> userPayBill : userPayBills) {
				for (String k : userPayBill.keySet()) {
					if (k.equals("pay_all_btc")) {
						String str = userPayBill.get(k).toString();
						Long payAllBtc = Long.valueOf(str);
						payAllBtcSum += payAllBtc;
					}
				}
			}
			map.put("page", billInfo);
			return map;
		}
		// 传了时间段按时间段搜索
		IPage<Map<String, Object>> pageList = btcUserPayBillMapperUtils.getBillInfoByDate(iPage, strTime, endTime);
		List<Map<String, Object>> userPayBills = pageList.getRecords();
		// 搜索出来的累计的payAllBtc;
		Long payAllBtcSum = 0L;
		for (Map<String, Object> userPayBill : userPayBills) {
			for (String k : userPayBill.keySet()) {
				if (k.equals("pay_all_btc")) {
					String str = userPayBill.get(k).toString();
					Long payAllBtc = Long.valueOf(str);
					payAllBtcSum += payAllBtc;
				}
			}
		}
		map.put("page", pageList);
		map.put("payAllBtcSum", payAllBtcSum);
		return map;
	}

	@Override
	public List<List<Object>> exportBillInfo(InBlock date) {
		Date strTime = date.getStrTime();
		Date endTime = date.getEndTime();
		List<List<Object>> exportList = new ArrayList<>();

		List<UserPayBill> list = btcUserPayBillMapperUtils.exportBillList(strTime, endTime);
		for (UserPayBill userPayBill : list) {
			List<Object> objects = new ArrayList<>();
			objects.add(userPayBill.getBillNumber());
			objects.add(userPayBill.getTxid());
			objects.add(userPayBill.getCteateAt());
			objects.add(BTCUtil.formatBTCNONull(userPayBill.getPayAllBtc()));
			objects.add(userPayBill.getStatus());// 支付状态
			objects.add(userPayBill.getUsername());// 操作人
			// 转成2维数组；
			exportList.add(objects);
		}
		return exportList;
	}

	@Override
	@Transactional
	public void rollBackBillItem(Long userId, String billNumber, List<Long> arrayList) {
		UserPayBill btcUserPayBill = btcUserPayBillMapperUtils.selectById(billNumber);
		if (btcUserPayBill == null) {
			throw new AdminException("bill.error", "回滚支付单错误，订单不存在");
		}
		if(btcUserPayBill.getStatus() == 0){
			throw new AdminException("bill.error", "回滚支付单错误，订单已完成支付");
		}

		if(arrayList == null || arrayList.isEmpty()){
			btcUserPayBillItemMapperUtils.updateAllRollBack(billNumber);
		}else{
			btcUserPayBillItemMapperUtils.updateListRollBack(billNumber, arrayList);
		}

		List<LinkedHashMap<String, Object>> billItemList = btcUserPayBillItemMapperUtils.getBillItemsByBillNumber(billNumber);
		//检测待支付账单里面是否还有子账单，如果没有则删除
		if(billItemList == null || billItemList.isEmpty()){
			btcUserPayBillMapperUtils.deleteById(billNumber);
		}else{
            Long allBtc = btcUserPayBillItemMapperUtils.getAllBtcByBillNumber(billNumber);
            btcUserPayBill.setPayCount(btcUserPayBill.getPayCount() - arrayList.size());
            btcUserPayBill.setPayAllBtc(allBtc);
			btcUserPayBillMapperUtils.updateById(btcUserPayBill);
        }

		//支付后通知管理员
		String adminId = userId+"";
		//notifyUtils.createBillNotify(adminId);
		//记录日志
		SysUser sysUser = AdminSecurityUtils.getUser();//获取当前登录的管理员
		executorService.execute(() -> {
			if (sysUser!= null){
				LogUserOperation model = new LogUserOperation();
				model.setLogType(2);//2矿池日志
				model.setUserType("admin");//操作人类型是管理员
				model.setCreatedTime(new Date());
				model.setUserId(sysUser.getUserId());
				if(arrayList == null || arrayList.isEmpty()){
					model.setContent("管理员["+sysUser.getUsername()+"]-回滚支付账单["+billNumber+"]，操作成功");
				}else{
					model.setContent("管理员["+sysUser.getUsername()+"]-回滚支付账单["+billNumber+"]中的子账单"+ arrayList.toString() + "，操作成功");
				}
				userOperationService.insert(model);
			}
		});
	}
}
