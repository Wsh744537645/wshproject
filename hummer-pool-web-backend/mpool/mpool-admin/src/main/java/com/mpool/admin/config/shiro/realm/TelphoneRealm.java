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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mpool.admin.config.shiro.TelphoneCodeCredentialsMatcher;
import com.mpool.admin.config.shiro.token.UsernamePasswordTelphoneToken;
import com.mpool.admin.exception.AdminException;
import com.mpool.admin.exception.ExceptionCode;
import com.mpool.admin.module.system.service.SysMenuService;
import com.mpool.admin.module.system.service.SysUserService;
import com.mpool.common.cache.service.PhoneCodeCahceService;
import com.mpool.common.model.admin.SysMenu;
import com.mpool.common.model.admin.SysUser;
import com.mpool.common.util.RedisKeys;

/**
 * 手机认证
 * 
 * @author chenjian2
 *
 */
@Component
public class TelphoneRealm extends AuthorizingRealm {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private PhoneCodeCahceService phoneCodeCahceService;

	public TelphoneRealm(TelphoneCodeCredentialsMatcher matcher) {
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

		UsernamePasswordTelphoneToken telphoneToken = (UsernamePasswordTelphoneToken) token;
		if (telphoneToken.getUsername() == null) {
			throw new AuthenticationException();// 没找到帐号
		}
//		if (telphoneToken.getTelphoneCode() == null) {
//			throw new AdminException(ExceptionCode.login_code_empty);
//		}

		String username = telphoneToken.getUsername();
		SysUser employee = sysUserService.findByUsername(username);
		if (telphoneToken.getUsername() == null) {
			throw new AuthenticationException();// 没找到帐号
		}		
		String code = phoneCodeCahceService.getCode(RedisKeys.getLoginSmsCode(username, employee.getTelphone()));
//		if (code == null) {
//			throw new AdminException(ExceptionCode.username_or_code_err);
//		}
		AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(employee, code, // 密码
				getName() // realm name
		);
		return authenticationInfo;
	}

}
