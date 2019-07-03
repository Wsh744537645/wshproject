package com.mpool.account.mapper.pool;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.pool.StatsUsersDay;

@Mapper
public interface StatsUsersDayMapper extends BaseMapper<StatsUsersDay> {

	List<StatsUsersDay> getStatsUsersDayInHourAndInPuid(@Param("days") List<String> past30Days,
			@Param("userIds") List<Long> userIds);

	List<StatsUsersDay> getStatsUsersDayInPuidAndDay(@Param("userIds") List<Long> userIds, @Param("day") String day);

}
