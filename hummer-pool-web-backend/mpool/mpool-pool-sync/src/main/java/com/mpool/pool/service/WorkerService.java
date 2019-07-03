package com.mpool.pool.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mpool.common.model.account.Worker;

public interface WorkerService {
	Integer checkRow(Map<String, Object> map);

	void insert(Map<String, Object> map);

	void inserts(List<Map<String, Object>> param);

	void replace(String regionId, List<Map<String, String>> param);

	public void updateWorkerStatus(Integer workerStatus, String regionId, List<String> puids, List<String> workerIds);

	void replace(List<Worker> list, String regionId, Date date);

	void replace(String regionId, List<Worker> workers, Date date);
}
