package com.mpool.share.service.impl;

import com.mpool.share.mapper.UserRoleMapper;
import com.mpool.share.service.UserRoleService;
import com.mpool.common.model.account.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
