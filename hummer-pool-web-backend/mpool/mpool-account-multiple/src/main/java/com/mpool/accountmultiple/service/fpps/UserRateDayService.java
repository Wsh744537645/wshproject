package com.mpool.accountmultiple.service.fpps;

import com.mpool.common.model.account.fpps.UserRateDay;

import java.util.List;

public interface UserRateDayService {
	List<UserRateDay> inserts(Integer day);

	UserRateDay findByPuidAndDay(Long puid, Integer day);

	List<UserRateDay> findByDay(Integer day);

	void insert(UserRateDay userRateDay);
}
