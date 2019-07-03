package com.mpool.account.service.fpps.impl;

import java.util.ArrayList;
import java.util.List;

import com.mpool.common.properties.MultipleProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpool.account.mapper.bill.UserPayMapper;
import com.mpool.account.mapper.fpps.UserRateDayMapper;
import com.mpool.account.service.fpps.UserRateDayService;
import com.mpool.common.model.account.bill.UserPay;
import com.mpool.common.model.account.fpps.UserRateDay;

@Service
public class UserRateDayServiceImpl implements UserRateDayService {

	@Autowired
	private UserRateDayMapper userRateDayMapper;
	@Autowired
	private UserPayMapper userPayMapper;
	@Autowired
	private MultipleProperties properties;

	@Override
	public List<UserRateDay> inserts(Integer day) {
		List<UserPay> userPayList = userPayMapper.selectList(new QueryWrapper<>());
		List<UserRateDay> userRateDays = new ArrayList<>();
		for (UserPay userPay : userPayList) {
			if(!userPay.getCurrencyId().equals(properties.getId())){
				continue;
			}

			Long puid = userPay.getPuid();
			Integer fppsRate = userPay.getFppsRate();

			UserRateDay userRateDay = new UserRateDay();
			userRateDay.setDay(day);
			userRateDay.setFppsRate(fppsRate);
			userRateDay.setPuid(puid);
			
			userRateDays.add(userRateDay);
		}
		userRateDayMapper.inserts(userRateDays);
		return userRateDays;
	}

	@Override
	public UserRateDay findByPuidAndDay(Long puid, Integer day) {
		return userRateDayMapper.findByPuidAndDay(puid, day);
	}

	@Override
	public List<UserRateDay> findByDay(Integer day) {
		return userRateDayMapper.findByDay(day);
	}

	@Override
	public void insert(UserRateDay userRateDay) {
		userRateDayMapper.insert(userRateDay);
	}

}
