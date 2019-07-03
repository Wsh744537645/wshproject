package com.mpool.accountmultiple.service.fpps;

import com.mpool.common.model.account.fpps.FppsRatioDay;

public interface FppsRatioDayService {
	void insert(FppsRatioDay fppsRatioDay);

	FppsRatioDay findByDay(Integer day);
}
