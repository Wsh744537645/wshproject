package com.mpool.share.config.shiro;

import com.mpool.share.config.shiro.token.UsernamePasswordTelphoneToken;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.springframework.stereotype.Component;

@Component
public class TelphoneCodeCredentialsMatcher extends SimpleCredentialsMatcher {
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		UsernamePasswordTelphoneToken token2 = (UsernamePasswordTelphoneToken) token;
		String tokenCode = token2.getTelphoneCode();
		String cacheCode = new String(toBytes(getCredentials(info)));
		return tokenCode.equals(cacheCode);
	}
}
