package com.mpool.account.config.shiro.realm;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import static com.mpool.account.constant.Constant.*;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mpool.account.config.shiro.TelphoneCodeCredentialsMatcher;
import com.mpool.account.config.shiro.token.UsernamePasswordTelphoneToken;
import com.mpool.account.exception.AccountException;
import com.mpool.account.exception.ExceptionCode;
import com.mpool.account.service.MenuService;
import com.mpool.account.service.UserService;
import com.mpool.common.cache.service.PhoneCodeCahceService;
import com.mpool.common.model.account.Menu;
import com.mpool.common.model.account.User;
import com.mpool.common.util.RedisKeys;

@Component
public class TelphoneRealm extends AuthorizingRealm {
	@Autowired
	private UserService usersService;
	@Autowired
	private MenuService sysMenuService;
	@Autowired
	private PhoneCodeCahceService phoneCodeCahceService;

	public TelphoneRealm(TelphoneCodeCredentialsMatcher matcher) {
		super(matcher);
	}

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

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		UsernamePasswordTelphoneToken telphoneToken = (UsernamePasswordTelphoneToken) token;
		if (telphoneToken.getUsername() == null) {
			throw new AuthenticationException();// 没找到帐号
		}
		if (telphoneToken.getTelphoneCode() == null) {
			throw new AccountException(ExceptionCode.login_code_empty);
		}

		String username = telphoneToken.getUsername();
		User user = usersService.findByUsername(username);
		if (user == null) {
			throw new AccountException(ExceptionCode.username_or_code_err);
		}
		String code = phoneCodeCahceService.getCode(RedisKeys.getLoginSmsCode(username, user.getUserPhone()));
		if (code == null) {
			throw new AccountException(ExceptionCode.username_or_code_err);
		}
		AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, code, // 密码
				getName() // realm name
		);
		return authenticationInfo;
	}

}
