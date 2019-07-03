package com.mpool.admin.mapperUtils.dashboard.mapperBase;

import com.mpool.common.model.pool.StatsUsersHour;
import com.mpool.common.model.pool.StatsWorkersHour;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/14 12:26
 */
public interface HpoolBaseMapper {
    List<StatsUsersHour> getStatsUsersHourInHour(@Param("userId") Long userId, @Param("list") List<String> hours);

    List<StatsWorkersHour> getStatsWorkerHourByWorkerId(@Param("workerId") Long workerId, @Param("list") List<String> hours);
}
