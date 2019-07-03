package com.mpool.account.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.account.model.CurrentWorkerStatus;
import com.mpool.common.model.account.Worker;
import com.mpool.common.model.account.WorkerGroup;
import com.mpool.common.model.pool.MiningWorkers;

/**
 * @author chenjian2
 *
 */
public interface WorkerService {

	/**
	 * 创建矿工组
	 * 
	 * @param workerGroup
	 */
	void createWorkerGruop(WorkerGroup workerGroup);

	/**
	 * @param page
	 * @param worker
	 * @return
	 */
	IPage<Worker> listWorker(IPage<Worker> page, Worker worker);

	/**
	 * @param
	 * @param workerGroup
	 * @return
	 */
	List<WorkerGroup> listWorkerGroup(WorkerGroup workerGroup);

	/**
	 * 修改矿工组
	 * 
	 * @param groupId
	 * @param asList
	 */
	void changeGroup(Long groupId, List<String> asList);

	/**
	 * 矿工列表
	 * 
	 * @param page
	 * @param
	 * @return
	 */
	IPage<Map<String, Object>> selectWorker(IPage<Long> page, Worker worker, Integer groupId);

	/**
	 * 编辑组
	 * 
	 * @param workerGroup
	 */
	void updateWorkerGruop(WorkerGroup workerGroup);

	/**
	 * 删除组
	 * 
	 * @param workerGroupId
	 * @param userId
	 */
	void deleteWorkerGruop(Long workerGroupId, Long userId);

	/**
	 * 删除矿工
	 * 
	 * @param workerId
	 * @param userId
	 */
	void deleteWorker(Long workerId, Long userId);

	/**
	 * 检测用户下面的 组名是否重复
	 * 
	 * @param userId
	 * @param groupName
	 */
	void checkExistByWorkerGroupName(Long userId, String groupName);

	/**
	 * 导出数据
	 * 
	 * @param userId
	 * @return
	 */
	List<List<Object>> exportWorker(Long userId);

	/**
	 * 批量删除
	 * 
	 * @param workerIds
	 * @param userId
	 */
	void deleteWorkerBatch(String userId, Long[] workerIds);

	/**
	 * 获得所有用户的 活跃矿工
	 * 
	 * @return
	 */
	List<CurrentWorkerStatus> getAllUserWorkerActive();

	List<CurrentWorkerStatus> countUserWorkerStatusBatch(List<Long> userIds);
}
