package com.mpool.admin.config.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.mpool.admin.config.shiro.token.UsernamePasswordTelphoneToken;

@Component
public class TelphoneCodeCredentialsMatcher extends SimpleCredentialsMatcher {

	private static final Logger log = LoggerFactory.getLogger(TelphoneCodeCredentialsMatcher.class);

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		log.debug("begin TelphoneCodeCredentialsMatcher doCredentialsMatch");
		boolean flag = false;
		try {
			UsernamePasswordTelphoneToken token2 = (UsernamePasswordTelphoneToken) token;
			String tokenCode = token2.getTelphoneCode();
			String cacheCode = new String(toBytes(getCredentials(info)));
			flag = tokenCode.equals(cacheCode);
		} catch (Exception e) {
			flag = false;
		}
		log.debug("end TelphoneCodeCredentialsMatcher doCredentialsMatch");
		return flag;
	}
}
