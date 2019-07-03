package com.mpool.account.mapper.pool;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StatsWorkersHourMapper {
	/**
	 * 获得矿工活跃值
	 * 
	 * @param days
	 * @param userIds
	 * @return puid ,hour ,activeNumber
	 */
	List<Map<String, Object>> getUserWorkerActiveByHour(@Param("hours") List<String> days,
			@Param("userIds") List<Long> userIds);
}
