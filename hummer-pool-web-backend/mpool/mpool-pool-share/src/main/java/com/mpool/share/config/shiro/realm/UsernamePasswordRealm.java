package com.mpool.share.config.shiro.realm;

import com.mpool.share.config.shiro.PasswordCredentialsMatcher;
import com.mpool.share.exception.ShareException;
import com.mpool.share.exception.ExceptionCode;
import com.mpool.share.service.UserService;
import com.mpool.share.model.User;
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

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Component
public class UsernamePasswordRealm extends AuthorizingRealm {

	private static final Logger log = LoggerFactory.getLogger(UsernamePasswordRealm.class);
	@Autowired
	private UserService usersService;


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
		//User user = (User) principals.getPrimaryPrincipal();
		return new SimpleAuthorizationInfo();
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
			throw new ShareException(ExceptionCode.username_or_password_err);
		}
		AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(), // 密码
				getName() // realm name
		);
		return authenticationInfo;
	}
}
