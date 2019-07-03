package com.mpool.share.admin.module.system.service;

import com.mpool.common.BaseService;
import com.mpool.common.model.admin.SysUserSysRole;

/**
 * 和角色
 * 
 * @author chenjian2
 *
 */
public interface SysUserSysRoleService extends BaseService<SysUserSysRole> {

	SysUserSysRole findByUserId(Long userId);

	void deleteByUserId(Long userId);

}
