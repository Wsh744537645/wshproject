package com.mpool.accountmultiple.service.impl;

import com.mpool.accountmultiple.mapper.pool.MiningWorkersMapper;
import com.mpool.mpoolaccountmultiplecommon.model.CurrentWorkerStatus;
import com.mpool.accountmultiple.service.AlarmService;
import com.mpool.accountmultiple.service.WorkerService;
import com.mpool.common.model.pool.MiningWorkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlarmServiceImpl implements AlarmService {

	private static final Logger log = LoggerFactory.getLogger(AlarmServiceImpl.class);

	@Autowired
	private MiningWorkersMapper miningWorkersMapper;
	@Autowired
	private WorkerService workerService;

	@Override
	public Map<Long, Integer> getUserWorkerActive() {
		List<CurrentWorkerStatus> workerStatusList = workerService.getAllUserWorkerActive();
		if (workerStatusList == null || workerStatusList.isEmpty()) {
			log.debug("-------------------No inactive worker were found-----------------------");
			return new HashMap<>();
		}
		Map<Long, Integer> hashMap = new HashMap<>();
		for (CurrentWorkerStatus status : workerStatusList) {
			Long puid = status.getUserId();
			Integer action = status.getCount();
			hashMap.put(puid, action);
		}
		log.debug("all user worker action => {}", hashMap);
		return hashMap;
	}

	@Override
	public Map<Long, Double> getUserShare() {
		List<MiningWorkers> list = miningWorkersMapper.getUserShare();
		if (list == null || list.isEmpty()) {
			log.debug("-------------------No active user were found-----------------------");
			return new HashMap<>();
		}
		Map<Long, Double> map = new HashMap<>();
		for (MiningWorkers miningWorkers : list) {
			Long puid = miningWorkers.getPuid();
			Double hashAccept15mT = miningWorkers.getHashAccept15mT();
			map.put(puid, hashAccept15mT);
		}
		log.debug("user share map => {}", map);
		return map;
	}

}
