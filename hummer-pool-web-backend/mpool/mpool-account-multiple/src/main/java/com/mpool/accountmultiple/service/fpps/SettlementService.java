package com.mpool.accountmultiple.service.fpps;

import java.util.Map;

public interface SettlementService {
	/**
	 * 收益结算方法
	 * 算力收益，推荐人收益，后续搞活动
	 * @param day
	 */
	void settlement(Integer day);

	void setUserFppsRate(Map<Long, Integer> map);
}
