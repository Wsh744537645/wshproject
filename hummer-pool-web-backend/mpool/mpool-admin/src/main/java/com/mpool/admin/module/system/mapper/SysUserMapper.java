package com.mpool.admin.module.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.admin.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

	SysUser findByUsername(@Param("username") String username);

	IPage<Map<String, Object>> getUserList(IPage<SysUser> iPage, @Param("user") SysUser sysUser);

	String getUsernameByUserId(@Param("userId") String userId);
	SysUser getAdminById(@Param("userId") String userId);
}
