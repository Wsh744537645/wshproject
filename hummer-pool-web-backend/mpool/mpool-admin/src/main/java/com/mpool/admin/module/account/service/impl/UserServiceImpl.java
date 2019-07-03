package com.mpool.admin.module.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.mapperUtils.bill.UserFppsRecordMapperUtils;
import com.mpool.admin.mapperUtils.bill.UserPayBillItemMapperUtils;
import com.mpool.admin.mapperUtils.pool.*;
import com.mpool.admin.module.account.mapper.UserMapper;
import com.mpool.admin.module.account.model.MasterUserShaerModel;
import com.mpool.admin.module.account.model.UserPayBillOut;
import com.mpool.admin.module.account.service.UserService;
import com.mpool.admin.module.common.EmailService;
import com.mpool.admin.module.pool.mapper.*;
import com.mpool.admin.module.pool.service.StatusUsersDayService;
import com.mpool.admin.module.recommend.mapper.RecommendUserMapper;
import com.mpool.admin.module.system.service.CurrencyService;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.admin.utils.NotifyUtils;
import com.mpool.common.model.account.User;
import com.mpool.common.model.account.bill.UserPayBillItem;
import com.mpool.common.model.account.fpps.UserFppsRecord;
import com.mpool.common.model.admin.SysUser;
import com.mpool.common.model.pool.MiningWorkers;
import com.mpool.common.model.pool.StatsUsersDay;
import com.mpool.common.model.pool.StatsUsersHour;
import com.mpool.common.model.pool.utils.MathShare;
import com.mpool.common.util.BTCUtil;
import com.mpool.common.util.DateUtil;
import com.mpool.common.util.EncryUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private StatusUsersDayService statusUsersDayService;
	@Autowired
	private MiningWorkersMapperUtils miningWorkersMapper;
	@Autowired
	private StatsUsersDayMapperUtils statsUsersDayMapper;
	@Autowired
	private UserPayBillItemMapperUtils userPayBillItemMapperUtils;
	@Autowired
	private UserFppsRecordMapperUtils userFppsRecordMapperUtils;
	@Autowired
	private RecommendUserMapper recommendUserMapper;
	@Autowired
	private StatsUsersHourMapperUtils statsUsersHourMapper;
	@Autowired
	private NotifyUtils notifyUtils;
//	发送邮件服务
	@Autowired
	private EmailService mailService;
	@Autowired
	private StatsWorkersDayMapperUtils statsWorkersDayMapper;
	@Autowired
	private StatsWorkersHourMapperUtils statsWorkersHourMapper;
	@Autowired
	private CurrencyService currencyService;

	@Override
	public void insert(User entity) {
		// TODO Auto-generated method stub
		// entity.setPassword();
	}

	@Override
	public void inserts(List<User> entitys) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(User entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Serializable id) {
		// TODO Auto-generated method stub

	}

	@Override
	public User findById(Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User findByUsername(String username) {
		User user = userMapper.getUserByname(username);
		return user;
	}

	@Override
	public IPage<Map<String, Object>> listPage(IPage<User> page, User user) {
		IPage<Map<String, Object>> selectPage = userMapper.selectMapsPage(page, new QueryWrapper<User>(user));
		// 去除密码
		selectPage.getRecords().stream().forEach(e -> {
			System.out.println(e.getClass());
		});
		return selectPage;
	}

	@Override
	public IPage<UserPayBillOut> masterListPage(IPage<User> page, User user) {
		IPage<UserPayBillOut> masterListPage = userMapper.masterListPage(page, user, currencyService.getCurCurrencyId());
		if (masterListPage.getRecords().size() == 0) {
			return masterListPage;
		}
		List<UserPayBillOut> masterUser = masterListPage.getRecords();
		Date date = DateUtil.addDay(new Date(), -1);
		String day = DateUtil.getYYYYMMdd(date);
		Map<Long, UserPayBillOut> temp = new HashMap<>();
		for (UserPayBillOut master : masterUser) {
			Long userId = master.getUserId();
			temp.put(userId, master);

			List<Long> puids = userMapper.getUserIdByMasterUserId(userId);
			//puids.add(userId);
			List<Long> pastDays = statusUsersDayService.getShareByPuidAndDay(puids, new Long(day));
			Double pastDayShare = pastDays.stream().map(MathShare::MathShareDayDouble).reduce(0d, (a, b) -> a + b);
			List<MiningWorkers> masterShare = miningWorkersMapper.getUserMiningWorkersList(puids);
			Double currentShare = masterShare.stream().map(L -> L.getHashAccept15mT()).reduce(0d, (a, b) -> a + b);
			//所有算力保留3位小数
			BigDecimal bg = new BigDecimal(currentShare);
			double num1 = bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
			master.setCurrentShare(num1);//当前算力
			BigDecimal bg2 = new BigDecimal(pastDayShare);
			double num2 = bg2.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
			master.setPastDayShare(num2);//过去1天算力
		}
		List<UserPayBillOut> userPayBillOutByPay = userMapper.masterListObjectPage(new ArrayList<>(temp.keySet()), currencyService.getCurCurrencyId());
		for (UserPayBillOut pay : userPayBillOutByPay) {
			Long userId = pay.getUserId();
			UserPayBillOut obj = temp.get(userId);
			// 余额
			obj.setTotalDue(pay.getTotalDue());
			// 累计转账
			obj.setTotalPaid(pay.getTotalPaid());
		}

		// 获得支付中

		List<UserPayBillItem> UserPayBill = userPayBillItemMapperUtils
				.getUserPayInPaymentByMasterId(new ArrayList<Long>(temp.keySet()));
		for (UserPayBillItem item : UserPayBill) {
			Long userId = item.getPuid();
			temp.get(userId).setInPayment(item.getPayBtc());
		}
		// 获得昨日收益
		List<UserFppsRecord> userFppsRecordList = userFppsRecordMapperUtils
				.masterUserFppsRecord(new ArrayList<Long>(temp.keySet()), day);
		for (UserFppsRecord item : userFppsRecordList) {
			Long userId = item.getPuid();
			temp.get(userId).setYesterday(item.getFppsAfterFee());
		}
		return masterListPage;
	}
	//double保留三位小数点
//	public static void main(String[] args) {
//		Double num = 40.6666d;
//		BigDecimal bg = new BigDecimal(num);
//		double num1 = bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
//		System.out.println(num1);
//	}

	@Override
	public IPage<UserPayBillOut> memberListPage(IPage<User> page, User user) {
		Date date = DateUtil.addDay(new Date(), -1);
		IPage<UserPayBillOut> userPayBillListOut = userMapper.memberListByObjectPage(page, user, currencyService.getCurCurrencyId());
		List<UserPayBillOut> memberUsers = userPayBillListOut.getRecords();
		if (memberUsers.isEmpty()) {
			return userPayBillListOut;
		}
		Map<Long, UserPayBillOut> temp = new HashMap<>();
		for (UserPayBillOut member : memberUsers) {

			String day = DateUtil.getYYYYMMdd(date);

			Long userId = member.getUserId();

			temp.put(userId, member);

			List<Long> userlist = new ArrayList<>(1);
			userlist.add(userId);

			MiningWorkers miningWorkers = miningWorkersMapper.getUserMiningWorkers(userId);
			if (miningWorkers != null) {
				Double hashAccept15mT = miningWorkers.getHashAccept15mT();
				member.setCurrentShare(hashAccept15mT);
			}
			Double sumPastDays = statusUsersDayService.getShareByPuidAndDay(userlist, new Long(day)).stream()
					.map(s -> MathShare.MathShareDayDouble(s)).reduce(0d, (a, b) -> a + b);
			member.setPastDayShare(sumPastDays);

			// 昨日收益
			UserFppsRecord fppsRecord = new UserFppsRecord();
			fppsRecord.setPuid(userId);
			fppsRecord.setDay(Integer.parseInt(day));
			UserFppsRecord selectOne = userFppsRecordMapperUtils.selectOne(fppsRecord);
			long yesterday = 0;
			if (selectOne != null) {
				yesterday = selectOne.getFppsAfterFee();
				member.setYesterday(yesterday);
			}
		}
		// 支付中
		List<UserPayBillItem> upbi = userPayBillItemMapperUtils.getUserPayInPayment(new ArrayList<>(temp.keySet()));
		for (UserPayBillItem userPayBillItem : upbi) {
			Long puid = userPayBillItem.getPuid();
			temp.get(puid).setInPayment(userPayBillItem.getPayBtc());
		}

		List<String> past24Hour = DateUtil.getPast24Hour();
		List<Long> users = new ArrayList<>(temp.keySet());
		Map<Long, List<StatsUsersHour>> users24Share = new HashMap<>();
		List<StatsUsersHour> statsUsersHours = statsUsersHourMapper.getUsers24HourShare(users, past24Hour);
		statsUsersHours.forEach(hour -> {
			Long puid = hour.getPuid();
			if (!users24Share.containsKey(puid)) {
				users24Share.put(puid, new ArrayList<>());
			}
			users24Share.get(puid).add(hour);
		});

		List<String> past30Days = DateUtil.getPast30Days();
		Map<Long, List<StatsUsersDay>> users30share = new HashMap<>();
		List<StatsUsersDay> statsUsersDays = statsUsersDayMapper.getStatsUsersDayInDayAndInPuid(users, past30Days);
		statsUsersDays.forEach(day -> {
			Long puid = day.getPuid();
			if (!users30share.containsKey(puid)) {
				users30share.put(puid, new ArrayList<>());
			}
			users30share.get(puid).add(day);
		});
		memberUsers.forEach(userInfo -> {
			Long userId = userInfo.getUserId();
			if (users30share.get(userId) != null) {
				double share30t = users30share.get(userId).stream()
						.collect(Collectors.averagingDouble(StatsUsersDay::getHashAcceptT));
				//所有算力保留三位小数点
				BigDecimal bg = new BigDecimal(share30t);
				double num1 = bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
				userInfo.setShare30T(num1);
			}
			if (users24Share.get(userId) != null) {
				double share24t = users24Share.get(userId).stream()
						.collect(Collectors.averagingDouble(StatsUsersHour::getHashAcceptT));
				//算力保留三位小数
				BigDecimal bg1 = new BigDecimal(share24t);
				double num2 = bg1.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
				userInfo.setShare24T(num2);
			}

		});
		return userPayBillListOut;
	}

	@Override
	public List<Map<String, Object>> getUser30DayWorker(Long userId) {
		List<String> days = DateUtil.getPast30Days();
		List<Map<String, Object>> list = statsWorkersDayMapper.getStatsUsersDayInDay(userId, days);

		if (list.size() != days.size()) {
			for (Map<String, Object> map : list) {
				String value = map.get("day").toString();
				days.remove(value);
			}
			for (String day : days) {
				Map<String, Object> map = new HashMap<>();
				map.put("day", Integer.parseInt(day));
				map.put("count", 0);
				list.add(map);
			}
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getUser24HWorker(Long userId) {
		List<String> hours = DateUtil.getPast24Hour();
		List<Map<String, Object>> list = statsWorkersHourMapper.getStatsUsersHourInHour(userId, hours);

		if (list.size() != hours.size()) {
			for (Map<String, Object> map : list) {
				String value = map.get("hour").toString();
				hours.remove(value);
			}
			for (String day : hours) {
				Map<String, Object> map = new HashMap<>();
				map.put("hour", Integer.parseInt(day));
				map.put("count", 0);
				list.add(map);
			}
		}
		return list;
	}

	@Override
	public List<StatsUsersDay> getUser30DayShare(Long userId) {
		List<String> get30Day = DateUtil.getPast30Days();
		List<StatsUsersDay> days = statsUsersDayMapper.getStatsUsersDayInDay(userId, get30Day);
		for (String day : get30Day) {
			StatsUsersDay statsUsersDay = new StatsUsersDay();
			statsUsersDay.setDay(new Long(day));
			if (!days.contains(statsUsersDay)) {
				days.add(statsUsersDay);
			}
		}
		return days;
	}

	@Override
	public List<MasterUserShaerModel> getMasterUser30DayShare(Long userId) {
		// 子账号Id
		List<Long> subUserIds = userMapper.getUserIdByMasterUserId(userId);
		if (subUserIds == null) {
			return new ArrayList<>();
		}
		// 最近30天
		List<String> days = DateUtil.getPast30Days();
		List<StatsUsersDay> lists = null;

		if (subUserIds != null && !subUserIds.isEmpty()) {
			lists = statsUsersDayMapper.getStatsUsersDayInDayAndInPuid(subUserIds, days);
		} else {
			lists = new ArrayList<>();
		}

		Map<Long, MasterUserShaerModel> result = new HashMap<>();
		for (int i = 0; i < days.size(); i++) {
			long day = Long.parseLong(days.get(i));
			result.put(day, new MasterUserShaerModel(day));
		}
		for (StatsUsersDay statsUsersDay : lists) {
			Long day = statsUsersDay.getDay();
			MasterUserShaerModel masterUserShaerModel = result.get(day);
			double hashAcceptT = statsUsersDay.getHashAcceptT();
			double hashReject = statsUsersDay.getHashRejectT();
			masterUserShaerModel.setShareAccept(hashAcceptT);
			masterUserShaerModel.setShareReject(hashReject);
		}
		Collection<MasterUserShaerModel> values = result.values();
		return new ArrayList<>(values);
	}

	@Override
	public List<UserPayBillOut> getUserListByUsername(String username) {
		IPage<UserPayBillOut> masterListPage = null;
		List<UserPayBillOut> masterUser = userMapper.getUserListByUsername(username, currencyService.getCurCurrencyId());
		Date date = DateUtil.addDay(new Date(), -1);
		String day = DateUtil.getYYYYMMdd(date);
		Map<Long, UserPayBillOut> temp = new HashMap<>();
		for (UserPayBillOut master : masterUser) {
			Long userId = master.getUserId();
			temp.put(userId, master);

			List<Long> puids = userMapper.getUserIdByMasterUserId(userId);
			puids.add(userId);
			List<Long> pastDays = statusUsersDayService.getShareByPuidAndDay(puids, new Long(day));
			Double pastDayShare = pastDays.stream().map(MathShare::MathShareDayDouble).reduce(0d, (a, b) -> a + b);
			List<MiningWorkers> masterShare = miningWorkersMapper.getUserMiningWorkersList(puids);
			Double currentShare = masterShare.stream().map(L -> L.getHashAccept15mT()).reduce(0d, (a, b) -> a + b);
			master.setCurrentShare(currentShare);
			master.setPastDayShare(pastDayShare);

		}
		List<UserPayBillOut> userPayBillOutByPay = userMapper.masterListObjectPage(new ArrayList<>(temp.keySet()), currencyService.getCurCurrencyId());
		for (UserPayBillOut pay : userPayBillOutByPay) {
			Long userId = pay.getUserId();
			UserPayBillOut obj = temp.get(userId);
			// 余额
			obj.setTotalDue(pay.getTotalDue());
			// 累计转账
			obj.setTotalPaid(pay.getTotalPaid());
		}

		// 获得支付中
		List<UserPayBillItem> UserPayBill = userPayBillItemMapperUtils
				.getUserPayInPaymentByMasterId(new ArrayList<Long>(temp.keySet()));
		for (UserPayBillItem item : UserPayBill) {
			Long userId = item.getPuid();
			temp.get(userId).setInPayment(item.getPayBtc());
		}
		// 获得昨日收益
		List<UserFppsRecord> userFppsRecordList = userFppsRecordMapperUtils
				.masterUserFppsRecord(new ArrayList<Long>(temp.keySet()), day);
		for (UserFppsRecord item : userFppsRecordList) {
			Long userId = item.getPuid();
			temp.get(userId).setYesterday(item.getFppsAfterFee());
		}
		return masterUser;
	}

	// 获取全部主账号信息
	@Override
	public List<UserPayBillOut> masterAllList(User user) {
		List<UserPayBillOut> masterUserAllList = userMapper.masterAllList(user, currencyService.getCurCurrencyId());
		Date date = DateUtil.addDay(new Date(), -1);
		String day = DateUtil.getYYYYMMdd(date);
		Map<Long, UserPayBillOut> temp = new HashMap<>();
		for (UserPayBillOut master : masterUserAllList) {
			Long userId = master.getUserId();
			temp.put(userId, master);

			List<Long> puids = userMapper.getUserIdByMasterUserId(userId);
			puids.add(userId);
			List<Long> pastDays = statusUsersDayService.getShareByPuidAndDay(puids, new Long(day));
			Double pastDayShare = pastDays.stream().map(MathShare::MathShareDayDouble).reduce(0d, (a, b) -> a + b);
			List<MiningWorkers> masterShare = miningWorkersMapper.getUserMiningWorkersList(puids);
			Double currentShare = masterShare.stream().map(L -> L.getHashAccept15mT()).reduce(0d, (a, b) -> a + b);
			master.setCurrentShare(currentShare);
			master.setPastDayShare(pastDayShare);

		}
		List<UserPayBillOut> userPayBillOutByPay = userMapper.masterListObjectPage(new ArrayList<>(temp.keySet()), currencyService.getCurCurrencyId());
		for (UserPayBillOut pay : userPayBillOutByPay) {
			Long userId = pay.getUserId();
			UserPayBillOut obj = temp.get(userId);
			// 余额
			obj.setTotalDue(pay.getTotalDue());
			// 累计转账
			obj.setTotalPaid(pay.getTotalPaid());
		}

		// 获得支付中

		List<UserPayBillItem> UserPayBill = userPayBillItemMapperUtils
				.getUserPayInPaymentByMasterId(new ArrayList<Long>(temp.keySet()));
		for (UserPayBillItem item : UserPayBill) {
			Long userId = item.getPuid();
			temp.get(userId).setInPayment(item.getPayBtc());
		}
		// 获得昨日收益
		List<UserFppsRecord> userFppsRecordList = userFppsRecordMapperUtils
				.masterUserFppsRecord(new ArrayList<Long>(temp.keySet()), day);
		for (UserFppsRecord item : userFppsRecordList) {
			Long userId = item.getPuid();
			temp.get(userId).setYesterday(item.getFppsAfterFee());
		}
		return masterUserAllList;
	}

	@Override
	public List<List<Object>> exportMasterList(User user) {

		List<List<Object>> list = new ArrayList<>();
		// 如果没有username参数导出全部的，如果有username则导出搜索的这条记录；
		String username = user.getUsername();

		if (username != "") {

			// 根据username搜索用户单条的主账号信息
			List<UserPayBillOut> masterList1 = getUserListByUsername(username);
			for (UserPayBillOut userPayBillOut : masterList1) {
				List<Object> objects = new ArrayList<>();
				objects.add(userPayBillOut.getUsername());
				// 当前总算力（15分钟）
				objects.add(userPayBillOut.getCurrentShare());
				// 过去一天的算力
				objects.add(userPayBillOut.getPastDayShare());
				// 昨日收益
				objects.add(BTCUtil.formatBTCNONull(userPayBillOut.getYesterday()));
				// 余额
				objects.add(BTCUtil.formatBTCNONull(userPayBillOut.getTotalDue()));
				// 总收益
				objects.add(BTCUtil.formatBTCNONull(userPayBillOut.getTotalRevenue()));
				// 累计转账
				objects.add(BTCUtil.formatBTCNONull(userPayBillOut.getTotalPaid()));
//				// 注册时间
//				objects.add(userPayBillOut.getCreateTime());
//				// 最后登录时间
//				objects.add(userPayBillOut.getLastTime());
				list.add(objects);
			}
			return list;
		}

		// 没有传username则导出全部主账号信息
		List<UserPayBillOut> masterAllList = masterAllList(user);
		List<List<Object>> li = new ArrayList<>();
		for (UserPayBillOut userPayBillOut : masterAllList) {
			List<Object> objects = new ArrayList<>();
			objects.add(userPayBillOut.getUsername());
			// 当前总算力（15分钟）
			objects.add(userPayBillOut.getCurrentShare());
			// 过去一天的算力
			objects.add(userPayBillOut.getPastDayShare());
			// 昨日收益
			objects.add(BTCUtil.formatBTCNONull(userPayBillOut.getYesterday()));
			// 余额
			objects.add(BTCUtil.formatBTCNONull(userPayBillOut.getTotalDue()));
			// 总收益
			objects.add(BTCUtil.formatBTCNONull(userPayBillOut.getTotalRevenue()));
			// 累计转账
			objects.add(BTCUtil.formatBTCNONull(userPayBillOut.getTotalPaid()));
			// 注册时间
			objects.add(userPayBillOut.getCreateTime());
			// 最后登录时间
			objects.add(userPayBillOut.getLastTime());
			list.add(objects);
		}
		return list;
	}

	@Override
	public IPage<User> getUserInfos(IPage<User> page, String username) {

		IPage<User> pageList = userMapper.getUserInfos(page, username);

		return pageList;
	}

//	@Override
//	public List<UserPayBillOut> getUserListByUsername(String username) {
////        IPage<UserPayBillOut> masterListPage =
//		IPage<UserPayBillOut> masterListPage = userMapper.getUserListByUsername(username);
//		List<UserPayBillOut> masterUser = masterListPage.getRecords();
//		Date date = DateUtil.addDay(new Date(), -1);
//		String day = DateUtil.getYYYYMMdd(date);
//		Map<Long, UserPayBillOut> temp = new HashMap<>();
//		for (UserPayBillOut master : masterUser) {
//			Long userId = master.getUserId();
//			temp.put(userId, master);
//
//			List<Long> puids = userMapper.getUserIdByMasterUserId(userId);
//			puids.add(userId);
//			List<Long> pastDays = statusUsersDayService.getShareByPuidAndDay(puids, new Long(day));
//			Double pastDayShare = pastDays.stream().map(MathShare::MathShareDayDouble).reduce(0d, (a, b) -> a + b);
//			List<MiningWorkers> masterShare = miningWorkersMapper.getUserMiningWorkersList(puids);
//			Double currentShare = masterShare.stream().map(L -> L.getHashAccept15mT()).reduce(0d, (a, b) -> a + b);
//			master.setCurrentShare(currentShare);
//			master.setPastDayShare(pastDayShare);
//
//		}
//		List<UserPayBillOut> userPayBillOutByPay = userMapper.masterListObjectPage(new ArrayList<>(temp.keySet()));
//		for (UserPayBillOut pay : userPayBillOutByPay) {
//			Long userId = pay.getUserId();
//			UserPayBillOut obj = temp.get(userId);
//			// 余额
//			obj.setTotalDue(pay.getTotalDue());
//			// 累计转账
//			obj.setTotalPaid(pay.getTotalPaid());
//		}
//
//		// 获得支付中
//		List<UserPayBillItem> UserPayBill = userPayBillItemMapperUtils
//				.getUserPayInPaymentByMasterId(new ArrayList<Long>(temp.keySet()));
//		for (UserPayBillItem item : UserPayBill) {
//			Long userId = item.getPuid();
//			temp.get(userId).setInPayment(item.getPayBtc());
//		}
//		// 获得昨日收益
//		List<UserFppsRecord> userFppsRecordList = userFppsRecordMapper
//				.masterUserFppsRecord(new ArrayList<Long>(temp.keySet()), day);
//		for (UserFppsRecord item : userFppsRecordList) {
//			Long userId = item.getPuid();
//			temp.get(userId).setYesterday(item.getFppsAfterFee());
//		}
//		return masterUser;
//	}

	@Override
	public void updatePasswordByUsername(String usrename) {
		SysUser adminUser = AdminSecurityUtils.getUser();
		Long id = adminUser.getUserId();
		String adminId = id + "";

		User user = userMapper.getUserByname(usrename);
		String userId = user.getUserId() + "";
		// 新密码加密
		String password = code() + "";
		user.setPassword(EncryUtil.encrypt(password));
		String psw = user.getPassword();

		// 重设密码
		userMapper.updatePasswordByUsername(psw, usrename);
		// 通知用户新密码 如果用户是手机注册则通知手机短信 否则邮箱
		String email = user.getUserEmail();
		if (email == null) {
			notifyUtils.modifyPasswordNotifyAccount(userId, password);
		}
		String phone = user.getUserPhone();
		if (phone == null) {
			sendMail(usrename, password);
		}

		// 通知管理员有用户密码重置
		notifyUtils.modifyPasswordNotifyAdmin(adminId, usrename);
	}

	// 生成密码
	private StringBuilder code() {
		Random rand = new Random();
		StringBuilder str = new StringBuilder();
		String strAll = "0123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";
		for (int i = 0; i < 8; i++) {
			int f = (int) (Math.random() * 56);
			str.append(strAll.charAt(f));
		}
		return str;
	}

	// 发送邮件服务 传
	private void sendMail(String usrename, String password) {
		User user = userMapper.getUserByname(usrename);
		if (user != null) {
			String email = user.getUserEmail();
			Map<String, Object> param = new HashMap<>();
			param.put("account_user", usrename);
			param.put("password", password);
			IContext contex = new Context(Locale.getDefault(), param);
			mailService.sendAlarmEmail(email, "notify/resetPasswordNotifyUser.html", "【蜂鸟】", contex);
		}
	}

	@Override
	public List<List<Object>> exportMasterUserInfoList(String username) {

		List<List<Object>> list = new ArrayList<>();
		List<User> lis = userMapper.getUserBy(username);
		for (User li : lis) {
			List<Object> objects = new ArrayList<>();
			objects.add(li.getUsername());// 主子用户名
			objects.add(li.getCreateTime());// 注册时间
			objects.add(li.getLastTime());// 最后登录时间
			objects.add(li.getUserPhone());// 注册手机
			objects.add(li.getUserEmail());// 注册有些
			objects.add(li.getMasterUserId() == null ? "主账号" : "子账号");// 主子账号标示 null为主
			list.add(objects);
		}
		return list;
	}

	@Override
	public boolean getCheckUsername(String username) {
		User user = new User();
		user.setUsername(username);
		Integer selectCount = userMapper.selectCount(new QueryWrapper<>(user));
		if (selectCount == null || selectCount.equals(0)) {
			return false;
		} else {
			return true;
		}
	}

}
