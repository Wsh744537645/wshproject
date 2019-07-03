package com.mpool.admin.mapperUtils.pool.mapperBase;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/5/14 16:17
 */
public interface StatsWorkersHourBaseMapper {

    Integer getActiveWorker(@Param("hour") String hour);
    List<Map<String, Object>> getStatsUsersHourInHour(@Param("userId") Long userId,
                                                      @Param("hours") List<String> hours);
}
