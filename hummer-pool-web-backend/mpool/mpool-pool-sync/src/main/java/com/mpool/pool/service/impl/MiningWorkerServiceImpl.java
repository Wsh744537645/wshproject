package com.mpool.pool.service.impl;

import java.util.Date;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mpool.pool.config.CanalConfig;
import com.mpool.pool.mapper.MiningWorkerMapper;
import com.mpool.pool.mapper.WorkerMapper;
import com.mpool.pool.service.MiningWorkerService;

@Service
public class MiningWorkerServiceImpl implements MiningWorkerService {
	@Autowired
	private MiningWorkerMapper miningWorkerMapper;
	@Autowired
	private WorkerMapper workerMapper;
	@Autowired
	private CanalConfig canalConfig;

	@Override
	public void delete(Map<String, Object> param) {
		miningWorkerMapper.delete(param);
	}

	@Override
	public void insert(Map<String, Object> param) {
		Integer checkRow = workerMapper.checkRow(param);
		if (checkRow == null || checkRow.equals(0)) {
			param.put("create_time", new Date());
			param.put("region_id", canalConfig.getRegionId());
			workerMapper.insert(param);
		}
		miningWorkerMapper.insert(param);
	}

	@Override
	public void update(Map<String, Object> param) {
		Integer checkRow = workerMapper.checkRow(param);
		if (checkRow == null || checkRow.equals(0)) {
			param.put("create_time", new Date());
			param.put("region_id", canalConfig.getRegionId());
			workerMapper.insert(param);
		}
		miningWorkerMapper.update(param);
	}

	@Override
	public Integer checkRow(Map<String, Object> param) {
		return miningWorkerMapper.checkRow(param);
	}

}
