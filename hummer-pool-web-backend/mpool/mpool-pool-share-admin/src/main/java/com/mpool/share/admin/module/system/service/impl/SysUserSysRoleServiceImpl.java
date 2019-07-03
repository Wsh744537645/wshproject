package com.mpool.share.admin.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpool.share.admin.module.system.mapper.SysUserSysRoleMapper;
import com.mpool.share.admin.module.system.service.SysUserSysRoleService;
import com.mpool.common.model.admin.SysUserSysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class SysUserSysRoleServiceImpl implements SysUserSysRoleService {
	@Autowired
	private SysUserSysRoleMapper sysUserSysRoleMapper;

	@Override
	public void insert(SysUserSysRole entity) {
		sysUserSysRoleMapper.insert(entity);
	}

	@Override
	public void inserts(List<SysUserSysRole> entitys) {
		// TODO Auto-generated method stub
	}

	@Override
	public void update(SysUserSysRole entity) {
		sysUserSysRoleMapper.updateById(entity);
	}

	@Override
	public void delete(Serializable id) {
		sysUserSysRoleMapper.deleteById(id);
	}

	@Override
	public SysUserSysRole findById(Serializable id) {
		return sysUserSysRoleMapper.selectById(id);
	}

	@Override
	public SysUserSysRole findByUserId(Long userId) {
		SysUserSysRole sysUserSysRole = new SysUserSysRole();
		sysUserSysRole.setUserId(userId);
		return sysUserSysRoleMapper.selectOne(new QueryWrapper<>(sysUserSysRole));
	}

	@Override
	public void deleteByUserId(Long userId) {
		SysUserSysRole sysUserSysRole = new SysUserSysRole();
		sysUserSysRole.setUserId(userId);
		sysUserSysRoleMapper.delete(new QueryWrapper<SysUserSysRole>(sysUserSysRole));
	}

}
