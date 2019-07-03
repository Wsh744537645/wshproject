package com.mpool.share.admin.module.system.service;

import com.mpool.common.BaseService;
import com.mpool.common.model.admin.SysRoleSysMenu;

import java.util.List;
import java.util.Map;

/**
 * 员工角色和菜单
 * 
 * @author chenjian2
 *
 */
public interface SysRoleSysMenuService extends BaseService<SysRoleSysMenu> {

	void insertRoleMenus(Integer roleId, List<Integer> asList);

	void deleteRoleAllMenu(Integer roleId);

	/**
	 * 获得角色下面有哪些菜单
	 * 
	 * @param roleId
	 * @return
	 */
	List<Map<String, Object>> getMenuByRole(Integer roleId);

}
