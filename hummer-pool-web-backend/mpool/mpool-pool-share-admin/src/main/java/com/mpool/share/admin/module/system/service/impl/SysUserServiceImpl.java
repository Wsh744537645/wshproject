package com.mpool.share.admin.module.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.share.admin.exception.AdminException;
import com.mpool.share.admin.module.system.mapper.SysUserMapper;
import com.mpool.share.admin.module.system.service.SysUserService;
import com.mpool.share.admin.module.system.service.SysUserSysRoleService;
import com.mpool.share.admin.utils.AccountSecurityUtils;
import com.mpool.share.admin.utils.AdminSecurityUtils;
import com.mpool.common.model.admin.SysUser;
import com.mpool.common.model.admin.SysUserSysRole;
import com.mpool.common.util.EncryUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SysUserServiceImpl implements SysUserService {
	@Autowired
	private SysUserMapper sysUserMapper;
	@Autowired
	private SysUserSysRoleService sysUserSysRoleService;


	@Override
	public SysUser getCurrentSysuser() {
		String userId = "" + AccountSecurityUtils.getUser().getUserId();
		return sysUserMapper.getAdminById(userId);
	}

	@Override
	public void insert(SysUser entity) {
		entity.setPassword(EncryUtil.encrypt(entity.getPassword()));
		sysUserMapper.insert(entity);
	}

	@Override
	public void inserts(List<SysUser> entitys) {

	}

	@Override
	public void update(SysUser entity) {
		sysUserMapper.updateById(entity);
	}

	@Override
	public void delete(Serializable id) {
		sysUserMapper.deleteById(id);

	}

	@Override
	public SysUser findById(Serializable id) {
		return sysUserMapper.selectById(id);
	}

	@Override
	public SysUser findByUsername(String username) {
		return sysUserMapper.findByUsername(username);
	}

	@Override
	public void changeUserRole(SysUserSysRole userRole) {
		SysUserSysRole sysUserSysRole = sysUserSysRoleService.findByUserId(userRole.getUserId());
		if (sysUserSysRole != null) {
			userRole.setId(sysUserSysRole.getId());
			sysUserSysRoleService.update(userRole);
		} else {
			sysUserSysRoleService.insert(userRole);
		}
	}

	@Override
	public IPage<Map<String, Object>> listPage(IPage<SysUser> iPage, SysUser sysUser) {
		IPage<Map<String, Object>> selectPage = sysUserMapper.getUserList(iPage, sysUser);
		return selectPage;
	}

	@Override
	public void updatePassword(SysUser sysUser, String oldPassword, String newPassword) {
		SysUser user = sysUserMapper.selectById(sysUser.getUserId());
		String encrypt = EncryUtil.encrypt(oldPassword);//EDS加密
		if (user.getPassword().equals(encrypt)) {
			String password = EncryUtil.encrypt(newPassword);
			user.setPassword(password);
		} else {
			throw new AdminException("password.error", "密码错误");
		}
		sysUserMapper.updateById(user);
		SecurityUtils.getSubject().logout();//修改完密码需要退出登录
	}

	@Override
	public void insert(SysUser sysUser, Integer roleId) {
		this.insert(sysUser);
		SysUserSysRole userRole = new SysUserSysRole();
		userRole.setCreateTime(new Date());
		userRole.setCreateBy(AdminSecurityUtils.getUser().getUserId());
		userRole.setRoleId(roleId);
		userRole.setUserId(sysUser.getUserId());
		sysUserSysRoleService.insert(userRole);
	}

	@Override
	public void deleteByUserId(Long userId) {
		SysUserSysRole findByUserId = sysUserSysRoleService.findByUserId(userId);
		Integer roleId = findByUserId.getRoleId();
		if (1 == roleId) {
			throw new AdminException("role.err", "系统管理员不能删除");
		}
		sysUserMapper.deleteById(userId);
		sysUserSysRoleService.deleteByUserId(userId);
	}

	@Override
	public SysUser findByUserById(String id) {

		return sysUserMapper.getAdminById(id);
	}

}
