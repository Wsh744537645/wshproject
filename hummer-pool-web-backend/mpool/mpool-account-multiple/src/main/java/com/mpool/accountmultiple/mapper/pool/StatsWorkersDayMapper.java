package com.mpool.accountmultiple.mapper.pool;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StatsWorkersDayMapper {
	/**
	 * 获得矿工活跃值
	 * @param days
	 * @param userIds
	 * @return puid ,day ,activeNumber
	 */
	List<Map<String, String>> getUserWorkerActive(@Param("days") List<String> days,
                                                  @Param("userIds") List<Long> userIds);
}
