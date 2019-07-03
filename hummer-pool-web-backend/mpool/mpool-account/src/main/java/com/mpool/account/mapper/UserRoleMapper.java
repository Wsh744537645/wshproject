package com.mpool.account.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.UserRole;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

	Integer findUserRoleId(@Param("userId")Long userId);

}
