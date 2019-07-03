package com.mpool.share.admin.module.system.service;

import com.mpool.common.BaseService;
import com.mpool.common.model.admin.SysRole;

import java.util.List;
import java.util.Map;

/**
 * 员工角色
 * 
 * @author chenjian2
 *
 */
public interface SysRoleService extends BaseService<SysRole> {

	List<Map<String, Object>> roleSelect();

	List<SysRole> list();
}
