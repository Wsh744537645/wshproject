package com.mpool.admin.mapperUtils.pool.mapperBase;

import com.mpool.common.model.pool.StatsUsersDay;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/14 16:00
 */
public interface StatsUsersDayBaseMapper {
    public List<Long> getShareByPuidAndDay(@Param("puids") List<Long> puid, @Param("day") Long day);

    public List<StatsUsersDay> getStatsUsersDayInDay(@Param("userId") Long userId,
                                                     @Param("list") List<String> get30Day);

    List<StatsUsersDay> getStatsUsersDayInDayAndInPuid(@Param("subUserIds") List<Long> subUserIds,
                                                       @Param("days") List<String> days);
}
