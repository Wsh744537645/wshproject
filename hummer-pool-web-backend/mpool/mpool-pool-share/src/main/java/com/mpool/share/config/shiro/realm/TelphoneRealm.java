package com.mpool.share.config.shiro.realm;

import com.mpool.share.config.shiro.TelphoneCodeCredentialsMatcher;
import com.mpool.share.config.shiro.token.UsernamePasswordTelphoneToken;
import com.mpool.share.exception.ShareException;
import com.mpool.share.exception.ExceptionCode;
import com.mpool.share.service.UserService;
import com.mpool.common.cache.service.PhoneCodeCahceService;
import com.mpool.share.model.User;
import com.mpool.common.util.RedisKeys;
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

@Component
public class TelphoneRealm extends AuthorizingRealm {
	@Autowired
	private UserService usersService;
	@Autowired
	private PhoneCodeCahceService phoneCodeCahceService;

	public TelphoneRealm(TelphoneCodeCredentialsMatcher matcher) {
		super(matcher);
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//User user = (User) principals.getPrimaryPrincipal();
		return new SimpleAuthorizationInfo();
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		UsernamePasswordTelphoneToken telphoneToken = (UsernamePasswordTelphoneToken) token;
		if (telphoneToken.getUsername() == null) {
			throw new AuthenticationException();// 没找到帐号
		}
//		if (telphoneToken.getTelphoneCode() == null) {
//			throw new ShareException(ExceptionCode.login_code_empty);
//		}

		String username = telphoneToken.getUsername();
		User user = usersService.findByUsername(username);
		if (user == null) {
			throw new ShareException(ExceptionCode.username_or_code_err);
		}
		String code = phoneCodeCahceService.getCode(RedisKeys.getLoginSmsCode(username, user.getUserPhone()));
		if (code == null) {
			throw new ShareException(ExceptionCode.username_or_code_err);
		}
		AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, code, // 密码
				getName() // realm name
		);
		return authenticationInfo;
	}

}
