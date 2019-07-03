package com.mpool.share.service;

public interface UserRoleService {
	/**
	 * 保存用户与角色的关系
	 * 
	 * @param userId
	 * @param roleId
	 */
	void saveUserRole(Long userId, Integer roleId);

	Integer findUserRoleId(Long userId);
}
