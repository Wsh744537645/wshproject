package com.mpool.admin.module.system.service;

import com.mpool.admin.config.shiro.token.UsernamePasswordTelphoneToken;
import com.mpool.common.model.admin.SysUser;

public interface AuthService {
	SysUser login(UsernamePasswordTelphoneToken token);

	void logout();
	
	
}
