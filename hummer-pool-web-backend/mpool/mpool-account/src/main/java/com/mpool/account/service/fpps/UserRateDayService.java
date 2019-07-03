package com.mpool.account.service.fpps;

import java.util.List;

import com.mpool.common.model.account.fpps.UserRateDay;

public interface UserRateDayService {
	List<UserRateDay> inserts(Integer day);

	UserRateDay findByPuidAndDay(Long puid, Integer day);

	List<UserRateDay> findByDay(Integer day);

	void insert(UserRateDay userRateDay);
}
