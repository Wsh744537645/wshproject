package com.mpool.account.mapper.pool;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.pool.MiningWorkers;
import com.mpool.common.model.pool.StatsUsersDay;
import com.mpool.common.model.pool.StatsUsersHour;

@Mapper
public interface PoolMapper {

	List<Map<String, Object>> getShare30Day(@Param("start") String start, @Param("end") String end);

	List<Map<String, Object>> getUser24H(@Param("userId") Long userId, @Param("start") String start,
			@Param("end") String end);

	List<Map<String, Object>> getWorker24Online(@Param("userId") Long userId, @Param("list") List<String> hours);

	/**
	 * 获取离线矿机
	 * 
	 * @param userId
	 * @return
	 */
	Integer getWorkerOffline(@Param("userId") Long userId);

	/**
	 * 获取所有矿机
	 * 
	 * @param userId
	 * @return
	 */
	Integer getAllWorkerByPuid(@Param("userId") Long userId);

	List<Map<String, Object>> getUser30DayShare(@Param("userId") Long userId, @Param("start") String start,
			@Param("end") String end);

	IPage<Map<String, Object>> getFoundBlocks(IPage<Map<String, Object>> page);

	List<StatsUsersHour> getStatsUserHour(@Param("userId") Long userId, @Param("start") String start,
			@Param("end") String end);

	/**
	 * in 查询30天
	 * 
	 * @param day30
	 * @return
	 */
	List<Map<String, Object>> getShareInDay(@Param("list") List<String> day30);

	/**
	 * in day 查询
	 * 
	 * @param userId
	 * @param days
	 * @return
	 */
	List<StatsUsersDay> getStatsUsersDayInDay(@Param("userId") Long userId, @Param("list") List<String> days);

	/**
	 * 
	 * @param userId
	 * @param hours
	 * @return
	 */
	List<StatsUsersHour> getStatsUsersHourInHour(@Param("userId") Long userId, @Param("list") List<String> hours);

	/**
	 * 根据puid 查询 MiningWorkers 5分钟 和15 分钟算力
	 * 
	 * @param userId
	 * @return
	 */
	MiningWorkers getMiningWorkersByPuid(Long userId);

	/**
	 * 获得用户所有矿机
	 * 
	 * @param userIds
	 * @return keys puid sum
	 */
	List<Map<String, Object>> getAllWorkerByPuidList(List<Long> userIds);

	/**
	 * 获得用户活跃的矿机数量
	 * 
	 * @param userIds
	 * @return puid ,sum
	 */
	List<Map<String, Object>> getActiveWorkerByPuidList(List<Long> userIds);

	/**
	 * 批量接口 获得用户实时算力状态
	 * @param userIds
	 * @return
	 */
	List<MiningWorkers> getMiningWorkersByPuidList(List<Long> userIds);

	/**
	 * wgg
	 * 从btc.com上面获取爆块记录
	 * @return
	 */
//	List<Map<String, Object>> getBtcComBlock();
}
