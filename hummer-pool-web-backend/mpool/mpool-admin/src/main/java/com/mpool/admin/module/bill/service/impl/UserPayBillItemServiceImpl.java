package com.mpool.admin.module.bill.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.mapperUtils.bill.UserPayBillItemMapperUtils;
import com.mpool.admin.mapperUtils.recommend.UserFeeRecordMapperUtils;
import com.mpool.admin.module.account.mapper.UserMapper;
import com.mpool.admin.module.bill.mapper.UserPayMapper;
import com.mpool.admin.module.bill.model.BillItemCSVModel;
import com.mpool.admin.module.bill.service.UserPayBillItemService;
import com.mpool.admin.module.recommend.mapper.UserFeeRecordMapper;
import com.mpool.admin.module.system.mapper.SysUserMapper;
import com.mpool.admin.module.system.service.CurrencyService;
import com.mpool.common.model.account.User;
import com.mpool.common.model.account.UserFeeRecord;
import com.mpool.common.model.account.bill.UserPay;
import com.mpool.common.model.account.bill.UserPayBillItem;
import com.mpool.common.util.BTCUtil;
import com.mpool.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class UserPayBillItemServiceImpl implements UserPayBillItemService {
	@Autowired
	private UserPayBillItemMapperUtils btcUserPayBillItemMapperUtils;
	@Autowired
	private SysUserMapper sysUserMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserPayMapper userPayMapper;
	@Autowired
	private UserFeeRecordMapperUtils userFeeRecordMapper;
	@Autowired
	private CurrencyService currencyService;

	@Override
	public IPage<Map<String, Object>> getDuePayItems(IPage<Map<String, Object>> iPage, Map<String, Object> param, Long day) {
		IPage<Map<String, Object>> pageList = btcUserPayBillItemMapperUtils.getDuePayItems(iPage, param, day);
		return pageList;
	}

	@Override
	public List<UserPayBillItem> getDuePayBtcSum(String username) {
		return btcUserPayBillItemMapperUtils.getDuePayBtcSum(username);
	}

	@Override
	public List<List<Object>> getCSVDataItems(String billNumber) {
		List<BillItemCSVModel> billItemCSVByBillNumber = btcUserPayBillItemMapperUtils
				.getBillItemCSVByBillNumber(billNumber);
		List<List<Object>> list = new ArrayList<>();
		// String[] header = new String[] { "Day", "Wallet Address", "cteate time",
		// "btc", "username" };
		// list.add(Arrays.asList(header));
		for (BillItemCSVModel billItemCSVModel : billItemCSVByBillNumber) {
			List<Object> objects = new ArrayList<>();
			// objects.add(billItemCSVModel.getDay());
			objects.add(billItemCSVModel.getWalletAddress());
			// objects.add(billItemCSVModel.getCteateAt());
			objects.add(billItemCSVModel.getPayBtc().toPlainString());
			// objects.add(billItemCSVModel.getUsername());
			list.add(objects);
		}
		return list;
	}

	@Override
	public List<LinkedHashMap<String, Object>> getBillItemInfo(String billNumber) {
		return btcUserPayBillItemMapperUtils.getBillItemsByBillNumber(billNumber);
	}

	@Override
	public IPage<Map<String, Object>> getPayBillItemList(IPage<Map<String, Object>> page, Map<String, Object> param) {
		IPage<Map<String, Object>> payBillItemList = btcUserPayBillItemMapperUtils.getPayBillItemList(page, param);
		payBillItemList.getRecords().forEach(m -> {
			Object object = m.get("payBy");
			if (object != null) {
				String usernameByUserId = sysUserMapper.getUsernameByUserId(object.toString());
				m.put("operat", usernameByUserId);
			}
		});
		return payBillItemList;
	}

	@Override
	public IPage<UserPayBillItem> getDuePayItemList(IPage<UserPayBillItem> iPage, String username) {

		return btcUserPayBillItemMapperUtils.getDuePayItemList(iPage, username);
	}

	@Override
	public List<List<Object>> exportDuePayItems(String username) {

		List<List<Object>> exportList = new ArrayList<>();

		List<UserPayBillItem> list = btcUserPayBillItemMapperUtils.exportDuePayItems(username);
		for (UserPayBillItem userPayBillItem : list) {
			List<Object> objects = new ArrayList<>();
			objects.add(userPayBillItem.getUsername());
			objects.add(userPayBillItem.getWalletAddress());// 钱包地址
			objects.add(BTCUtil.formatBTCNONull(userPayBillItem.getPayBtc()));// 金额
			objects.add(userPayBillItem.getCteateAt());
			exportList.add(objects);
		}
		return exportList;
	}

	@Override
	public void addPayBillInfo(UserPayBillItem userPayBillItem) {
		String yyyymMdd = DateUtil.getYYYYMMdd(new Date());
		String username = userPayBillItem.getUsername();
		User user = userMapper.getUserByname(username);
		if (user == null) {
			throw new RuntimeException("该用户不存在");
		}
		Long puid = user.getUserId();
		UserPay userPay = userPayMapper.getUserPayInfo(puid, currencyService.getCurCurrencyId());
		UserPayBillItem model = new UserPayBillItem();
		model.setPuid(puid);
		model.setWalletAddress(userPay.getWalletAddress());// 钱包地址
		model.setDesc(userPayBillItem.getDesc());// 费用类型描述 备注栏
		model.setPayBtc(userPayBillItem.getPayBtc());
		model.setCteateAt(new Date());
		model.setDay(Long.parseLong(yyyymMdd));
		btcUserPayBillItemMapperUtils.addPayBillInfo(model);

		//插入收益记录表
		List<UserFeeRecord> lis = new ArrayList<>();
		UserFeeRecord userFeeRecord = new UserFeeRecord();
		userFeeRecord.setUserId(puid);
		userFeeRecord.setDay(Integer.parseInt(yyyymMdd));
		userFeeRecord.setFee(userPayBillItem.getPayBtc());
		userFeeRecord.setCreateTime(new Date());
		userFeeRecord.setFeeType(userPayBillItem.getFeeType());
		userFeeRecord.setFeeDesc(userPayBillItem.getDesc());
		lis.add(userFeeRecord);
		userFeeRecordMapper.insets(lis);
	}

	@Transactional
	@Override // 如果传来的payBtc是加
	public void updateListPayBct(Double payBtcRate) {
		// 获取全部带生成账单
		List<UserPayBillItem> list = btcUserPayBillItemMapperUtils.getUserPayBillItemList();
		for (UserPayBillItem userPayBillItem : list) {
			Long id = userPayBillItem.getId();
			Long btcDou = userPayBillItem.getPayBtc();
			Long sumBtc = btcDou + BigDecimal.valueOf(btcDou).multiply(BigDecimal.valueOf(payBtcRate)).longValue();

			btcUserPayBillItemMapperUtils.updateListPayBct(sumBtc, id);
//			}

		}

	}

}
