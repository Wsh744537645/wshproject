package com.mpool.accountmultiple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.UserRegion;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRegionMapper extends BaseMapper<UserRegion> {

	String findRegionNameByUserId(@Param("userId") Long userId);

	Integer findRegionIdByUserId(@Param("userId") Long userId);

}
