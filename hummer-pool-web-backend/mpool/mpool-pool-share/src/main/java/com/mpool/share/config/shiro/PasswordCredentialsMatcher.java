package com.mpool.share.config.shiro;

import com.mpool.share.config.shiro.cache.PasswordRetryCache;
import com.mpool.common.util.EncryUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chen 验证码密码
 *
 */
@Configuration
public class PasswordCredentialsMatcher extends SimpleCredentialsMatcher {

	private static final Logger log = LoggerFactory.getLogger(PasswordCredentialsMatcher.class);

	// Cache<String, AtomicInteger> passwordRetryCache;
	@Autowired
	private PasswordRetryCache passwordRetryCache;

//	public CustomCredentialsMatcher(Cache<String, AtomicInteger> passwordRetryCache) {
//		this.passwordRetryCache = passwordRetryCache;
//	}

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		String username = ((UsernamePasswordToken) token).getUsername();
		AtomicInteger retryCount = passwordRetryCache.get(username);
		if (retryCount == null) {
			retryCount = new AtomicInteger(0);
		}
//		if (retryCount.incrementAndGet() > 5) {
//			log.debug("username ->{}, retry count -> {}", username, retryCount);
//			throw new ExcessiveAttemptsException();
//		}

		String string = new String(toBytes(token.getCredentials()));
		Object tokenCredentials = EncryUtil.encrypt(string).getBytes();
		Object accountCredentials = getCredentials(info);
		boolean matches = equals(tokenCredentials, accountCredentials);
		if (matches) {
			passwordRetryCache.remove(username);
		} else {
			passwordRetryCache.put(username, retryCount);
		}
		return matches;

	}
}
