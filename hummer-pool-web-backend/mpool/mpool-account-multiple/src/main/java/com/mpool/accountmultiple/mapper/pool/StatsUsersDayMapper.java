package com.mpool.accountmultiple.mapper.pool;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.pool.StatsUsersDay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StatsUsersDayMapper extends BaseMapper<StatsUsersDay> {

	List<StatsUsersDay> getStatsUsersDayInHourAndInPuid(@Param("days") List<String> past30Days,
                                                        @Param("userIds") List<Long> userIds);

	List<StatsUsersDay> getStatsUsersDayInPuidAndDay(@Param("userIds") List<Long> userIds, @Param("day") String day);

}
