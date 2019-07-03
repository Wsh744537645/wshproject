package com.mpool.admin.module.system.service;

import java.util.List;
import java.util.Map;

import com.mpool.common.BaseService;
import com.mpool.common.model.admin.SysRole;

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
