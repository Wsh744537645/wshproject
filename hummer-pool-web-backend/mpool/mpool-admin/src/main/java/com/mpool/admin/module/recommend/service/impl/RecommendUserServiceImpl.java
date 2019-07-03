package com.mpool.admin.module.recommend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.InModel.InBlock;
import com.mpool.admin.InModel.OutUserFppsRatio;
import com.mpool.admin.exception.AdminException;
import com.mpool.admin.exception.ExceptionCode;
import com.mpool.admin.mapperUtils.recommend.RecommendApartCoinMapperUtils;
import com.mpool.admin.module.account.mapper.UserMapper;
import com.mpool.admin.module.bill.mapper.UserPayMapper;
import com.mpool.admin.module.dashboard.service.impl.DashboardServiceImpl;
import com.mpool.admin.module.recommend.mapper.RecommendApartCoinMapper;
import com.mpool.admin.module.recommend.mapper.RecommendUserMapper;
import com.mpool.admin.module.recommend.mapper.UserFppsRateMapper;
import com.mpool.admin.module.recommend.service.RecommendUserService;
import com.mpool.admin.module.system.service.CurrencyService;
import com.mpool.common.model.account.RecommendUser;
import com.mpool.common.model.account.User;
import com.mpool.common.model.account.UserFeeRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RecommendUserServiceImpl implements RecommendUserService {

	private static final Logger log = LoggerFactory.getLogger(DashboardServiceImpl.class);

	@Autowired
	RecommendUserMapper recommendUserMapper;
	@Autowired
	UserMapper userMapper;
	@Autowired
	RecommendApartCoinMapperUtils recommendApartCoinMapper;
	@Autowired
	UserFppsRateMapper userFppsRateMapper;
	@Autowired
	UserPayMapper userPayMapper;
	@Autowired
	private CurrencyService currencyService;

	@Override
	public void creatRecommendUser(RecommendUser entity) {
		RecommendUser recommendUser = new RecommendUser();
		String name = entity.getRecommendName();

		User user = userMapper.getUserByname(name);
		if (user == null) {
			throw new AdminException(ExceptionCode.username_null_err);
		}
		Long master = user.getMasterUserId();
		if (master == null){
			throw new AdminException(ExceptionCode.not_member_user);
		}
		recommendUser.setRecommendId(user.getUserId());
		recommendUser.setRecommendName(user.getUsername());
		recommendUser.setCreatedTime(new Date());
		recommendUser.setUserId(entity.getUserId());
		recommendUser.setFeeRate(entity.getFeeRate());
		recommendUser.setMasterId(user.getCreateBy());
		recommendUser.setCurrencyId(currencyService.getCurCurrencyId());

		Long uId = entity.getUserId();
		RecommendUser ruser = recommendUserMapper.getUserByUserId(uId);
		log.debug("被推荐人user_id="+uId);
		if (ruser != null){
			recommendUserMapper.updateRecommendUser(recommendUser);
		}else {
			recommendUserMapper.creatRecommendUser(recommendUser);
		}
	}

	@Override
	public void deleteRecommendUser(Serializable id) {
		recommendUserMapper.deleteRecommendUser(id);
	}

	@Override
	public IPage<OutUserFppsRatio> getFppsRateAndUserTypeList(IPage<OutUserFppsRatio> page, OutUserFppsRatio in) {
		String username = in.getUsername();
		Integer userGroup = in.getUsreGroup();
		if (username == null && userGroup == null) {
			IPage<OutUserFppsRatio> pageList = userFppsRateMapper.getAllMemberUserList(page, currencyService.getCurCurrencyId());
			return pageList;
		}
		if (username != null) {
			User user = userMapper.getMasterUserByname(username);
			if (user == null) {
				throw new RuntimeException("主用户名输入错误");
			}
		}
		if (username != null && userGroup == null) {
			User user = userMapper.getMasterUserByname(username);
			Long userId = user.getUserId();
			IPage<OutUserFppsRatio> pageList = userFppsRateMapper.getFppsRateAndUserTypeListMaster(page, userId, currencyService.getCurCurrencyId());
			return pageList;
		}
		if (username == null && userGroup != null) {
			IPage<OutUserFppsRatio> pageList = userFppsRateMapper.getAllMemberUserListGroup(page, userGroup, currencyService.getCurCurrencyId());
			return pageList;
		}
		if (username != null && userGroup != null) {
			User user = userMapper.getMasterUserByname(username);
			Long userId = user.getUserId();
			IPage<OutUserFppsRatio> pageList = userFppsRateMapper.getAllMemberUserLists(page, userId, userGroup, currencyService.getCurCurrencyId());
			return pageList;
		}
		return null;
	}

	@Override
	public List<List<Object>> exportHistoryFppsRateAndUserTypeList(IPage<OutUserFppsRatio> page, OutUserFppsRatio in) {

		List<List<Object>> exportlist = new ArrayList<>();

		IPage<OutUserFppsRatio> pageList = getFppsRateAndUserTypeList(page, in);
		List<OutUserFppsRatio> lis = pageList.getRecords();
		for (OutUserFppsRatio li : lis) {
			List<Object> objects = new ArrayList<>();
			objects.add(li.getUsername());
			objects.add(li.getUsreGroup());
			objects.add(li.getFppsRate());
			objects.add(li.getPayMode());
//            objects.add(li.getUserId());
			exportlist.add(objects);
		}
		return exportlist;
	}

	@Override
	public Map<String, Object> getHistoryApartCoin(IPage<UserFeeRecord> page, InBlock date) {
		Date strTime = date.getStrTime();
		Date endTime = date.getEndTime();
		//传进来的时间段减去23小时59分59秒
		if (endTime != null) {
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(endTime);
			rightNow.add(Calendar.HOUR, 23);
			rightNow.add(Calendar.MINUTE, 59);
			rightNow.add(Calendar.SECOND, 59);
			endTime = rightNow.getTime();
		}
		// 总分币
		Map<String, Object> map = new HashMap<>();
		IPage<UserFeeRecord> feePage = recommendApartCoinMapper.getHistoryApartCoin(page, strTime, endTime);

		Map<Integer, UserFeeRecord> tmp = new HashMap<>();
		for (UserFeeRecord userFee : feePage.getRecords()) {
			Date date1 = userFee.getCreateTime();
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			String str = df.format(date1);
			Integer day = Integer.valueOf(str);
			userFee.setDay(day);
//			Integer day = userFee.getDay();
			tmp.put(day, userFee);
		}

		// 算当日推荐
		if (!map.isEmpty()) {
			List<UserFeeRecord> records = recommendApartCoinMapper
					.getHistoryRecommendApartCoin(new ArrayList<>(tmp.keySet()));
			for (UserFeeRecord userFeeRecord : records) {
				UserFeeRecord userFeeRecord2 = tmp.get(userFeeRecord.getDay());
				userFeeRecord2.setRecommendFeeSum(userFeeRecord.getFeeSum());
			}
		}

		// 获得总分币 和推荐分币
		Long recommendFeeSum = recommendApartCoinMapper.sumApartCoin(2, strTime, endTime);
		Long feeSum = recommendApartCoinMapper.sumApartCoin(null, strTime, endTime);

		map.put("page", feePage);
		map.put("feeSum", feeSum);
		map.put("recommendFeeSum", recommendFeeSum);
		return map;
	}

	@Override
	public List<List<Object>> exportHistoryApartCoinList(IPage<UserFeeRecord> page, InBlock date) {
		List<List<Object>> exportlist = new ArrayList<>();
		Date strTime = date.getStrTime();
		Date endTime = date.getEndTime();
		if (endTime != null) {
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(endTime);
			rightNow.add(Calendar.HOUR, 23);
			rightNow.add(Calendar.MINUTE, 59);
			rightNow.add(Calendar.SECOND, 59);
			endTime = rightNow.getTime();
		}
		IPage<UserFeeRecord> pages = recommendApartCoinMapper.getHistoryApartCoin(page, strTime, endTime);
		List<UserFeeRecord> list = pages.getRecords();
		for (UserFeeRecord userFeeRecord : list) {
			List<Object> objects = new ArrayList<>();
			objects.add(userFeeRecord.getDay());
			objects.add(userFeeRecord.getFeeSum());
			objects.add(userFeeRecord.getRecommendFeeSum());
			exportlist.add(objects);
		}
		return exportlist;
	}


	@Override
	public Long getCurrentCoin(Date day) {
		return recommendApartCoinMapper.getCurrentCoin(day);
	}
}
