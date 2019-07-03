package com.mpool.admin.module.system.service;

import java.util.List;
import java.util.Set;

import com.mpool.admin.module.system.model.MenuModel;
import com.mpool.common.BaseService;
import com.mpool.common.model.admin.SysMenu;

/**
 * 员工菜单
 * 
 * @author chenjian2
 *
 */
public interface SysMenuService extends BaseService<SysMenu> {
	List<SysMenu> findByUserId(Long userId);

	Set<MenuModel> transformMenuModel(List<SysMenu> sysMenus);

	List<SysMenu> getList();
}
