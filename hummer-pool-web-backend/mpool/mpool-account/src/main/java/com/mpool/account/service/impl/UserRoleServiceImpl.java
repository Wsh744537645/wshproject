package com.mpool.account.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mpool.account.mapper.UserRoleMapper;
import com.mpool.account.service.UserRoleService;
import com.mpool.common.model.account.UserRole;

@Service
public class UserRoleServiceImpl implements UserRoleService {
	@Autowired
	private UserRoleMapper sysUserRoleMapper;

	@Override
	public void saveUserRole(Long userId, Integer roleId) {
		UserRole entity = new UserRole(userId, roleId);

		sysUserRoleMapper.insert(entity);
	}

	@Override
	public Integer findUserRoleId(Long userId) {
		return sysUserRoleMapper.findUserRoleId(userId);
	}

}
