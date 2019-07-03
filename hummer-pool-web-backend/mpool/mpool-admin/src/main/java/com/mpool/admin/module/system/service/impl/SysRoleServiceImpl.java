package com.mpool.admin.module.system.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpool.admin.module.system.mapper.SysRoleMapper;
import com.mpool.admin.module.system.service.SysRoleService;
import com.mpool.common.model.admin.SysRole;
import com.mpool.common.model.aspect.annotation.Insert;

@Service
public class SysRoleServiceImpl implements SysRoleService {
	@Autowired
	private SysRoleMapper sysRoleMapper;

	@Override
	@Insert
	public void insert(SysRole entity) {
		sysRoleMapper.insert(entity);
	}

	@Override
	public void inserts(List<SysRole> entitys) {
	}

	@Override
	public void update(SysRole entity) {
		sysRoleMapper.updateById(entity);
	}

	@Override
	public void delete(Serializable id) {
		sysRoleMapper.deleteById(id);
	}

	@Override
	public SysRole findById(Serializable id) {
		return sysRoleMapper.selectById(id);
	}

	@Override
	public List<Map<String, Object>> roleSelect() {
		QueryWrapper<SysRole> queryWrapper = new QueryWrapper<SysRole>();
		queryWrapper.select("role_id as roleId", "role_name as roleName");
		List<Map<String, Object>> selectMaps = sysRoleMapper.selectMaps(queryWrapper);
		return selectMaps;
	}

	@Override
	public List<SysRole> list() {
		return sysRoleMapper.selectList(null);
	}

}
