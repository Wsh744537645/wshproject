package com.mpool.account.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.account.model.UserWorkerStatus;
import com.mpool.account.model.dashboard.SubUserStatus;
import com.mpool.account.model.dashboard.UserShareChart;
import com.mpool.account.model.dashboard.UserStatus;
import com.mpool.common.model.account.User;
import com.mpool.common.model.pool.MiningWorkers;
import com.mpool.common.model.pool.StatsUsersDay;

public interface PoolService {

	/**
	 * 获取池近30天算力
	 * 
	 * @param end
	 * @param start
	 * 
	 * @return
	 */
	List<Map<String, Object>> getShare30Day(String start, String end);

	/**
	 * 获取24 小时算力
	 * 
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getUser24H(Long userId);

	/**
	 * 获取24小时 worker 在线数
	 * 
	 * @param userId
	 * @param start
	 * @param end
	 * @return
	 */
	List<Map<String, Object>> getWorker24Online(Long userId, String start, String end);

	/**
	 * 当前worker 状态
	 * 
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getCurrentWorkerStatu(Long userId);

	/**
	 * 获取30 天算力
	 * 
	 * @param userId
	 * @return
	 */
	List<StatsUsersDay> getUser30DayShare(Long userId);

	/**
	 * 获取爆块记录
	 * 
	 * @param page
	 * @return
	 */
	IPage<Map<String, Object>> getFoundBlocks(IPage<Map<String, Object>> page);

	/**
	 * 获得算力 Dashbaord
	 * 
	 * @param userId
	 * @return
	 */
	Map<String, Double> getUserShareDashboard(Long userId);

	List<Map<String, Object>> getShare30Day(List<String> get30Day);

	/**
	 * 获得矿工信息
	 * 
	 * @param workerId
	 * @param userId
	 * @return
	 */
	MiningWorkers getMiningWorkers(Long workerId, Long userId);

	/**
	 * 获得今天未支付 昨天已支付
	 * 
	 * @param userId
	 * @param today
	 * @param yesterday
	 * @return
	 */
	Map<String, Object> getUserPay(Long userId);

	List<UserStatus> getMasterRuntimeInfo(List<User> subUser);

	/**
	 * 获得用户的 算力 24 小时
	 * 
	 * @param subUser
	 * @return
	 */
	Map<String, List<UserShareChart>> getMasterShareChartInfoBy24(List<User> subUser);

	Map<String, List<UserShareChart>> getMasterShareChartInfoBy30(List<User> subUser);

	/**
	 * 获得用户最近24小时运行状态
	 * 
	 * @param subUser
	 * @return
	 */
	Map<String, List<UserWorkerStatus>> getMasterWorkerStatusBy24(List<User> subUser);

	SubUserStatus getSubUserRuntimeInfo(User user);
}
