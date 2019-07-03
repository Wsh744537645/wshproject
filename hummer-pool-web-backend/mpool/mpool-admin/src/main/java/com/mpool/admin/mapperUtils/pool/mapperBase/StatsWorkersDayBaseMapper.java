package com.mpool.admin.mapperUtils.pool.mapperBase;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/5/14 16:12
 */
public interface StatsWorkersDayBaseMapper {
    List<Map<String, Object>> getPoolPast30DayWorker(@Param("days") List<String> days);

    List<Map<String, Object>> getStatsUsersDayInDay(@Param("userId") Long userId,
                                                    @Param("days") List<String> get30Day);
}
