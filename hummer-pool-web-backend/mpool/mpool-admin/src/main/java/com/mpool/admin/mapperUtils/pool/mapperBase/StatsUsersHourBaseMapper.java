package com.mpool.admin.mapperUtils.pool.mapperBase;

import com.mpool.common.model.pool.StatsUsersHour;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/14 16:07
 */
public interface StatsUsersHourBaseMapper {
    List<StatsUsersHour> getUsers24HourShare(@Param("puids") List<Long> arrayList,
                                             @Param("hours") List<String> past24Hour);
}
