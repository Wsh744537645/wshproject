package com.mpool.pool.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mpool.common.model.account.Worker;
import com.mpool.pool.mapper.WorkerMapper;
import com.mpool.pool.service.WorkerService;

@Service
public class WorkerServiceImpl implements WorkerService {
	@Autowired
	private WorkerMapper workerMapper;

	@Override
	public Integer checkRow(Map<String, Object> map) {
		return workerMapper.checkRow(map);
	}

	@Override
	public void insert(Map<String, Object> map) {
		workerMapper.insert(map);
	}

	@Override
	public void inserts(List<Map<String, Object>> param) {
		workerMapper.inserts(param);
	}

	@Override
	public void replace(String regionId, List<Map<String, String>> param) {
		workerMapper.replace(regionId, param);
	}

	@Override
	public void updateWorkerStatus(Integer workerStatus, String regionId, List<String> puids, List<String> workerIds) {
		workerMapper.updateWorkerStatus(workerStatus, regionId, puids, workerIds);
	}

	@Override
	public void replace(List<Worker> list, String regionId, Date date) {
		workerMapper.replaceByWorker(list, regionId, date);
	}

	@Override
	public void replace(String regionId, List<Worker> workers, Date date) {
		workerMapper.replaceAll(regionId, workers, date);
	}

}
