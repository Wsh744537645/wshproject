package com.mpool.admin.module.dashboard.service;

import com.mpool.common.model.pool.MiningWorkers;

import java.util.List;
import java.util.Map;

public interface PoolApiService {
	MiningWorkers getCurrentShare();

	/**
	 * 获取24 小时算力
	 *
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getUser24H(Long userId);

	/**
	 * 获取矿机24小时信息
	 * @param workerId
	 * @return
	 */
	List<Map<String, Object>> getWorker24HByWorkerId(Long workerId);
}
