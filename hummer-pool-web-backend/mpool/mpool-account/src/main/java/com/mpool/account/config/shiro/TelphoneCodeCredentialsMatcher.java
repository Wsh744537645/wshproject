package com.mpool.account.config.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Component;

import com.mpool.account.config.shiro.token.UsernamePasswordTelphoneToken;

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
