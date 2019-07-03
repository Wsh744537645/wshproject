package com.mpool.accountmultiple.mapper.pool;

import com.mpool.common.model.pool.StatsUsersHour;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StatsUsersHourMapper {
	List<StatsUsersHour> getStatsUsersHourInHourAndInPuid(@Param("hours") List<String> hours, @Param("userIds") List<Long> userIds);
}
