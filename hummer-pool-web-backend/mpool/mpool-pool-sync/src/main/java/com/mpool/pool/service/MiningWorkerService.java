package com.mpool.pool.service;

import java.util.Map;

public interface MiningWorkerService {
	void delete(Map<String, Object> param);

	void insert(Map<String, Object> param);

	void update(Map<String, Object> param);

	Integer checkRow(Map<String, Object> param);
}
