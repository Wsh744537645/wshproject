package com.mpool.admin.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpool.admin.exception.AdminException;
import com.mpool.admin.exception.ExceptionCode;
import com.mpool.admin.module.system.mapper.SysRoleSysMenuMapper;
import com.mpool.admin.module.system.service.SysRoleSysMenuService;
import com.mpool.common.model.admin.SysRoleSysMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleSysMenuServiceImpl implements SysRoleSysMenuService {
	@Autowired
	private SysRoleSysMenuMapper sysRoleSysMenuMapper;

	@Override
	public void insert(SysRoleSysMenu entity) {
		sysRoleSysMenuMapper.insert(entity);
	}

	@Override
	public void inserts(List<SysRoleSysMenu> entitys) {
		for (SysRoleSysMenu sysRoleSysMenu : entitys) {
			insert(sysRoleSysMenu);
		}
	}

	@Override
	public void update(SysRoleSysMenu entity) {
		sysRoleSysMenuMapper.updateById(entity);
	}

	@Override
	public void delete(Serializable id) {
		sysRoleSysMenuMapper.deleteById(id);
	}

	@Override
	public SysRoleSysMenu findById(Serializable id) {
		return sysRoleSysMenuMapper.selectById(id);
	}

	@Override
	public void insertRoleMenus(Integer roleId, List<Integer> asList) {
		List<SysRoleSysMenu> entitys = new ArrayList<SysRoleSysMenu>();
		for (Integer menuId : asList) {
				SysRoleSysMenu sysMenuRoleByRole =	sysRoleSysMenuMapper.getSysMenuRoleByRole(roleId);
			if (sysMenuRoleByRole != null){
				Integer memuID = sysMenuRoleByRole.getMenuId();
				if (memuID == menuId){
					throw new AdminException(ExceptionCode.menu_exists);
				}
			}
			SysRoleSysMenu menu = new SysRoleSysMenu();
			menu.setRoleId(roleId);
			menu.setMenuId(menuId);
			entitys.add(menu);
		}
		this.inserts(entitys);
	}

	@Override
	public void deleteRoleAllMenu(Integer roleId) {
		SysRoleSysMenu menu = new SysRoleSysMenu();
		menu.setRoleId(roleId);
		QueryWrapper<SysRoleSysMenu> queryWrapper = new QueryWrapper<SysRoleSysMenu>(menu);
		sysRoleSysMenuMapper.delete(queryWrapper);
	}

	@Override
	public List<Map<String, Object>> getMenuByRole(Integer roleId) {
		return sysRoleSysMenuMapper.getMenuByRole(roleId);
	}

}
