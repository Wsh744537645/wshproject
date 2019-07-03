package com.mpool.admin.config.shiro.realm;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mpool.admin.config.shiro.PasswordCredentialsMatcher;
import com.mpool.admin.exception.AdminException;
import com.mpool.admin.exception.ExceptionCode;
import com.mpool.admin.module.system.service.SysMenuService;
import com.mpool.admin.module.system.service.SysUserService;
import com.mpool.common.model.admin.SysMenu;
import com.mpool.common.model.admin.SysUser;

/**
 * 密码认证
 * 
 * @author chenjian2
 *
 */
@Component
public class UsernamePasswordRealm extends AuthorizingRealm {

	private static final Logger log = LoggerFactory.getLogger(UsernamePasswordRealm.class);
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysMenuService sysMenuService;

	public UsernamePasswordRealm(PasswordCredentialsMatcher matcher) {
		super(matcher);
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SysUser user = (SysUser) principals.getPrimaryPrincipal();
		List<SysMenu> menus = sysMenuService.findByUserId(user.getUserId());
		Set<String> perms = new TreeSet<>();
		if (menus != null && !menus.isEmpty()) {
			for (SysMenu sysMenu : menus) {
				if (!StringUtils.isEmpty(sysMenu.getPerms())) {
					perms.addAll(Arrays.asList(sysMenu.getPerms().split(",")));
				}
			}
		}
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		simpleAuthorizationInfo.setStringPermissions(perms);
		return simpleAuthorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		if (token.getPrincipal() == null) {
			throw new AuthenticationException();// 没找到帐号
		}
		String username = (String) token.getPrincipal();
		SysUser user = sysUserService.findByUsername(username);
		if (user == null) {
			throw new AuthenticationException();// 没找到帐号
		}
		AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(), // 密码
				getName() // realm name
		);
		return authenticationInfo;
	}
}
