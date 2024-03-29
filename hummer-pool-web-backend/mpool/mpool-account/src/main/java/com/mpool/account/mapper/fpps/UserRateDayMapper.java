package com.mpool.account.mapper.fpps;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.fpps.UserRateDay;

@Mapper
public interface UserRateDayMapper extends BaseMapper<UserRateDay> {
	int inserts(@Param("list") List<UserRateDay> list);

	UserRateDay findByPuidAndDay(@Param("puid") Long puid, @Param("day") Integer day);

	List<UserRateDay> findByDay(@Param("day") Integer day);
}
