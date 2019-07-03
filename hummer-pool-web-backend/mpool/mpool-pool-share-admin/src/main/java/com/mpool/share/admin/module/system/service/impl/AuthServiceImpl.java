package com.mpool.share.admin.module.system.service.impl;

import com.mpool.share.admin.config.shiro.token.UsernamePasswordTelphoneToken;
import com.mpool.share.admin.module.system.service.AuthService;
import com.mpool.common.model.admin.SysUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

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
