package com.mpool.account.config.shiro.realm;

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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mpool.account.config.shiro.PasswordCredentialsMatcher;
import com.mpool.account.exception.AccountException;
import com.mpool.account.exception.ExceptionCode;
import com.mpool.account.service.MenuService;
import com.mpool.account.service.UserService;
import com.mpool.common.model.account.Menu;
import com.mpool.common.model.account.User;

@Component
public class UsernamePasswordRealm extends AuthorizingRealm {

	private static final Logger log = LoggerFactory.getLogger(UsernamePasswordRealm.class);
	@Autowired
	private UserService usersService;
	@Autowired
	private MenuService sysMenuService;


	public UsernamePasswordRealm(PasswordCredentialsMatcher matcher) {
		super(matcher);
	}

	/**
	 * 授权
	 * @param principals
	 * @return
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		User user = (User) principals.getPrimaryPrincipal();
		List<Menu> menus = sysMenuService.findByUserId(user.getUserId());
		Set<String> perms = new TreeSet<>();
		if (menus != null && !menus.isEmpty()) {
			for (Menu sysMenu : menus) {
				if (!StringUtils.isEmpty(sysMenu.getPerms())) {
					perms.addAll(Arrays.asList(sysMenu.getPerms().split(",")));
				}
			}
		}
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		simpleAuthorizationInfo.setStringPermissions(perms);
		return simpleAuthorizationInfo;
	}

	/**
	 * 认证
	 * @param token
	 * @return
	 * @throws AuthenticationException
	 */

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		if (token.getPrincipal() == null) {
			throw new AuthenticationException();// 没找到帐号
		}
		String username = (String) token.getPrincipal();
		User user = usersService.findByUsername(username);
		if (user == null) {
			throw new AccountException(ExceptionCode.username_or_password_err);
		}
		AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(), // 密码
				getName() // realm name
		);
		return authenticationInfo;
	}
}
