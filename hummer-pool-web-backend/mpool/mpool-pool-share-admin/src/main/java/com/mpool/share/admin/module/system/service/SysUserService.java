package com.mpool.share.admin.module.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.BaseService;
import com.mpool.common.model.admin.SysUser;
import com.mpool.common.model.admin.SysUserSysRole;

import java.util.Map;

/**
 * 员工
 * 
 * @author chenjian2
 *
 */
public interface SysUserService extends BaseService<SysUser> {
	SysUser getCurrentSysuser();

	SysUser findByUsername(String username);

	void changeUserRole(SysUserSysRole userRole);

	IPage<Map<String, Object>> listPage(IPage<SysUser> iPage, SysUser sysUser);

	void updatePassword(SysUser sysUser, String oldPassword, String newPassword);

	void insert(SysUser sysUser, Integer roleId);

	void deleteByUserId(Long userId);

	SysUser findByUserById(String id);
}
