package com.mpool.account.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.UserRegion;

import io.lettuce.core.dynamic.annotation.Param;

@Mapper
public interface UserRegionMapper extends BaseMapper<UserRegion> {

	String findRegionNameByUserId(@Param("userId") Long userId);

	Integer findRegionIdByUserId(@Param("userId") Long userId);

}
