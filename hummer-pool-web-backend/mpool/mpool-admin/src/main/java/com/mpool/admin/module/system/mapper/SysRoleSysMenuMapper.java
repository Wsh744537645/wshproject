package com.mpool.admin.module.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.admin.SysRoleSysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysRoleSysMenuMapper extends BaseMapper<SysRoleSysMenu> {

	List<Map<String, Object>> getMenuByRole(@Param("roleId") Integer roleId);

	SysRoleSysMenu getSysMenuRoleByRole(Integer roleId);
}
