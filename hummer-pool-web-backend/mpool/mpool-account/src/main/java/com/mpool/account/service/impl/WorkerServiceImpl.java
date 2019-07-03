package com.mpool.account.service.impl;

import java.util.*;

import com.mpool.common.model.account.WorkerVo;
import com.mpool.common.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.account.exception.AccountException;
import com.mpool.account.exception.ExceptionCode;
import com.mpool.account.mapper.WorkerGroupMapper;
import com.mpool.account.mapper.WorkerGroupWorkerMapper;
import com.mpool.account.mapper.WorkerMapper;
import com.mpool.account.mapper.pool.MiningWorkersMapper;
import com.mpool.account.model.CurrentWorkerStatus;
import com.mpool.account.service.PoolService;
import com.mpool.account.service.UserRegionSerice;
import com.mpool.account.service.WorkerService;
import com.mpool.account.utils.AccountSecurityUtils;
import com.mpool.common.model.account.Worker;
import com.mpool.common.model.account.WorkerGroup;
import com.mpool.common.model.account.WorkerGroupWorker;
import com.mpool.common.model.pool.MiningWorkers;
import com.mpool.common.util.page.Page;

@Service
public class WorkerServiceImpl implements WorkerService {

	private static final Logger log = LoggerFactory.getLogger(WorkerServiceImpl.class);

	@Autowired
	private WorkerMapper workerMapper;
	@Autowired
	private WorkerGroupMapper workerGroupMapper;
	@Autowired
	private UserRegionSerice userRegionService;
	@Autowired
	private PoolService poolService;
	@Autowired
	private WorkerGroupWorkerMapper workerGroupWorkerMapper;

	@Override
	public void createWorkerGruop(WorkerGroup workerGroup) {
		WorkerGroup entity = new WorkerGroup();
		entity.setGroupName(workerGroup.getGroupName());
		entity.setUserId(workerGroup.getUserId());

		List<WorkerGroup> selectList = workerGroupMapper.selectList(new QueryWrapper<WorkerGroup>(entity));
		if (selectList != null && !selectList.isEmpty()) {
			throw new AccountException("mpool.account.workerGroup.repeat.create", "您重复创建工作组！");
		}
		workerGroup.setStatus("1");
		Integer regionId = userRegionService.findRegionIdByUserId(workerGroup.getUserId());
		workerGroup.setRegionId(regionId);
		workerGroupMapper.insert(workerGroup);
	}

	@Override
	public IPage<Worker> listWorker(IPage<Worker> page, Worker worker) {
		return workerMapper.selectPage(page, new QueryWrapper<Worker>(worker));
	}

	@Override
	public List<WorkerGroup> listWorkerGroup(WorkerGroup workerGroup) {
		List<WorkerGroup> selectPage = workerGroupMapper.selectListOrderByGroupSeq(workerGroup);
		return selectPage;
	}

	@Override
	public void changeGroup(Long groupId, List<String> workerIds) {
		if (workerIds == null || workerIds.isEmpty()) {
			throw new AccountException(ExceptionCode.worker_ids_empty);
		}
		Long userId = AccountSecurityUtils.getUser().getUserId();

		if (groupId == 0L) {
			workerGroupWorkerMapper.deleteWorkerGroup(workerIds, userId);
		} else {
			for (String workerId : workerIds) {
				WorkerGroupWorker workerGroupWorker = new WorkerGroupWorker();
				workerGroupWorker.setUserId(userId);
				workerGroupWorker.setWorkerId(Long.parseLong(workerId));
				QueryWrapper<WorkerGroupWorker> queryWrapper = new QueryWrapper<WorkerGroupWorker>(workerGroupWorker);
				WorkerGroupWorker selectOne = workerGroupWorkerMapper.selectOne(queryWrapper);

				if (selectOne == null) {
					workerGroupWorker.setGroupId(groupId);
					workerGroupWorkerMapper.insert(workerGroupWorker);
				} else {
					WorkerGroupWorker entity = new WorkerGroupWorker();
					entity.setGroupId(groupId);
					entity.setWorkerId(workerGroupWorker.getWorkerId());
					entity.setUserId(userId);
					workerGroupWorkerMapper.update(entity, queryWrapper);
				}
			}
		}
	}

	@Override
	public IPage<Map<String, Object>> selectWorker(IPage<Long> page, Worker worker, Integer groupId) {
		IPage<Map<String, Object>> result = new Page<>(page);
		int isSelect = 1;
		if(groupId != null){
			if(groupId != -1) {
				isSelect = workerGroupWorkerMapper.getWorkerCount(worker.getWorkerId(), worker.getUserId(), groupId);
			}
		}
		if(isSelect > 0) {
			result = workerMapper.selectWorkerIdAndWorkerName(result, worker, groupId, DateUtil.addDay(new Date(), -1));
			List<Map<String, Object>> tmpList = result.getRecords();
			for (Map<String, Object> map : tmpList) {
				WorkerVo worker1 = new WorkerVo(map);
				worker1.resetMap(map);
			}
		}
		return result;
	}

	@Override
	public void updateWorkerGruop(WorkerGroup workerGroup) {
		WorkerGroup workerGroupEntity = workerGroupMapper.selectById(workerGroup.getGroupId());
		if (workerGroupEntity == null) {
			throw new AccountException(ExceptionCode.group_notfound);
		}
		workerGroupEntity.setGroupName(workerGroup.getGroupName());
		workerGroupEntity.setGroupSeq(workerGroup.getGroupSeq());
		workerGroupMapper.updateById(workerGroupEntity);
	}

	@Override
	@Transactional
	public void deleteWorkerGruop(Long workerGroupId, Long userId) {
		WorkerGroup entity = new WorkerGroup();
		entity.setGroupId(workerGroupId);
		entity.setUserId(userId);
		WorkerGroup selectOne = workerGroupMapper.selectOne(new QueryWrapper<WorkerGroup>(entity));
		if (selectOne == null) {
			throw new AccountException(ExceptionCode.group_notfound);
		}
		WorkerGroupWorker workerGroupWorker = new WorkerGroupWorker();
		workerGroupWorker.setGroupId(workerGroupId);
		workerGroupWorker.setUserId(userId);
		QueryWrapper<WorkerGroupWorker> wrapper = new QueryWrapper<>(workerGroupWorker);
		workerGroupWorkerMapper.delete(wrapper);
		workerGroupMapper.deleteById(workerGroupId);
	}

	@Override
	public void deleteWorker(Long workerId, Long userId) {
		Worker worker = new Worker();
		worker.setWorkerId(workerId);
		worker.setUserId(userId);
		QueryWrapper<Worker> wrapper = new QueryWrapper<>(worker);
		MiningWorkers miningWorkers = poolService.getMiningWorkers(workerId, userId);
		if (miningWorkers != null) {
			throw new AccountException("1000001", "该矿工还在提交算力");
		}
		WorkerGroupWorker workerGroupWorker = new WorkerGroupWorker();
		workerGroupWorker.setWorkerId(workerId);
		workerGroupWorker.setUserId(userId);
		workerGroupWorkerMapper.delete(new QueryWrapper<>(workerGroupWorker));
		worker.setRegionId(userRegionService.findRegionIdByUserId(userId));
		workerMapper.delete(wrapper);
	}

	@Override
	public void checkExistByWorkerGroupName(Long userId, String groupName) {
		WorkerGroup group = workerGroupMapper.findByWorkerGroupName(userId, groupName);
		if (group != null) {
			throw new AccountException(ExceptionCode.worker_group_exist);
		}
	}

	@Override
	public List<List<Object>> exportWorker(Long userId) {
		List<Worker> exprotData = workerMapper.exprotData(userId);
		List<List<Object>> list = new ArrayList<>();
		for (Worker worker : exprotData) {
			List<Object> arrayList = new ArrayList<Object>();
			// Object[] he = { "矿工名", "15分钟平均算力", "1小时平均算力", "15分钟拒绝率", "1小时拒绝率", "最近提交时间",
			// "矿机状态" };
			arrayList.add(worker.getWorkerName());
			arrayList.add(worker.getHashAccept15mT());
			arrayList.add(worker.getHashAccept1hT());
			arrayList.add(worker.getRateReject15m());
			arrayList.add(worker.getRateReject1h());
			arrayList.add(worker.getLastShareTime());
			arrayList.add(worker.getStatus());
			list.add(arrayList);
		}
		return list;
	}

	@Override
	public void deleteWorkerBatch(String userId, Long[] workerIds) {
		// 只能删除 worker_status = 2 的
		Long[] ids = workerIds;
		for (int i = 0; i < workerIds.length; i++) {
			Long id = ids[i];
			Worker workerInfo = workerMapper.getWorkerInfo(id);
			if (workerInfo.getWorkerStatus() == 2) {
				workerMapper.deleteByWorkerId(userId, id);
			} else {
				throw new RuntimeException("删除的信息有误！");
			}
		}

	}

	@Override
	public List<CurrentWorkerStatus> getAllUserWorkerActive() {
		Date date = new Date();
		date = DateUtil.addDay(date, -1);
		date = DateUtil.addMinute(date, -1);
		//mining_workers表只会保存24小时数据，account_worker表中有一些24小时之前的数据没有删除，所以需要加时间进行筛选
		return workerMapper.getAllUserWorkerActive(date);
	}

	@Override
	public List<CurrentWorkerStatus> countUserWorkerStatusBatch(List<Long> userIds) {
		List<Worker> workerList = workerMapper.countUserWorkerStatusBatch(userIds);
		List<CurrentWorkerStatus> workerStatuses = new ArrayList<>();
		Map<Long, Integer> countMap0 = new HashMap<>();
		Map<Long, Integer> countMap1 = new HashMap<>();
		Map<Long, Integer> countMap2 = new HashMap<>();
		for(Worker worker : workerList){
			Long userId = worker.getUserId();
			Integer status = worker.getWorkerStatus();
			if(status.equals(0)) {
				if (countMap0.get(userId) == null) {
					countMap0.put(userId, 1);
				} else {
					countMap0.put(userId, countMap0.get(userId) + 1);
				}
				continue;
			}

			if(status.equals(1)) {
				if (countMap1.get(userId) == null) {
					countMap1.put(userId, 1);
				} else {
					countMap1.put(userId, countMap1.get(userId) + 1);
				}
				continue;
			}

			if(status.equals(2)) {
				if (countMap2.get(userId) == null) {
					countMap2.put(userId, 1);
				} else {
					countMap2.put(userId, countMap2.get(userId) + 1);
				}
			}
		}

		for(Long key : countMap0.keySet()){
			workerStatuses.add(new CurrentWorkerStatus(key, countMap0.get(key), 0));
		}
		for(Long key : countMap1.keySet()){
			workerStatuses.add(new CurrentWorkerStatus(key, countMap1.get(key), 1));
		}
		for(Long key : countMap2.keySet()){
			workerStatuses.add(new CurrentWorkerStatus(key, countMap2.get(key), 2));
		}

		return workerStatuses;
	}
}
