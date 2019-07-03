package com.mpool.admin.module.system.service.impl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import com.mpool.admin.config.shiro.token.UsernamePasswordTelphoneToken;
import com.mpool.admin.module.system.service.AuthService;
import com.mpool.common.model.admin.SysUser;

@Service
public class AuthServiceImpl implements AuthService {

	@Override
	public SysUser login(UsernamePasswordTelphoneToken token) {
		Subject subject = SecurityUtils.getSubject();
		subject.login(token);
		SysUser user = (SysUser) subject.getPrincipals().getPrimaryPrincipal();
		return user;
	}

	@Override
	public void logout() {
		SecurityUtils.getSubject().logout();
	}

}
