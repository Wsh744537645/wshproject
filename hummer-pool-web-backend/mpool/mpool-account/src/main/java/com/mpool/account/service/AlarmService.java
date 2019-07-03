package com.mpool.account.service;

import java.util.Map;

/**
 * 告警服务
 * 
 * @author chen9
 *
 */
public interface AlarmService {
	/**
	 * 获得用户不活跃的矿工
	 * @return
	 */
	Map<Long, Integer> getUserWorkerActive();

	/**
	 * 获得用户的算力
	 * @return
	 */
	Map<Long, Double> getUserShare();
}
