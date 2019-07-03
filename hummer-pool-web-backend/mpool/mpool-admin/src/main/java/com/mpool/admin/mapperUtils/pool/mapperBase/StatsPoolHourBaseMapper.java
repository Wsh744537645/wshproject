package com.mpool.admin.mapperUtils.pool.mapperBase;

import com.mpool.common.model.pool.StatsPoolHour;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/14 15:56
 */
public interface StatsPoolHourBaseMapper {
    StatsPoolHour getStatsPoolHourByHour(@Param("hour") String hour);

    /**
     * 查询一个时间的集合
     *
     * @param hours
     * @return
     */
    List<StatsPoolHour> getStatsPoolHourByHours(@Param("hours") List<String> hours);
}
