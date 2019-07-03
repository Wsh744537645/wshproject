package com.mpool.account.mapper.pool;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mpool.common.model.pool.StatsUsersHour;

@Mapper
public interface StatsUsersHourMapper {
	List<StatsUsersHour> getStatsUsersHourInHourAndInPuid(@Param("hours")List<String> hours,@Param("userIds") List<Long> userIds);
}
