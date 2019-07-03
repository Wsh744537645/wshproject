package com.mpool.common.util;

import org.apache.shiro.SecurityUtils;

public class RedisKeys {
	private static String getSessionId() {
		return SecurityUtils.getSubject().getSession().getId().toString();
	}

	public static String getSysConfigKey(String key) {
		return "sys:config:" + key;
	}

	public static String getShiroSessionKey(String key) {
		return "sessionid:" + key;
	}

	/**
	 * 获取短信登录key
	 * 
	 * @param username
	 * @param telphone
	 * @return
	 */
	public static String getLoginSmsCode(String username, String telphone) {

		return "login:sms:" + getSessionId() + ":" + username + ":" + telphone;
	}

	public static String getRegSmsCode(String phoneNumber) {
		return "reg:sms:" + getSessionId() + ":" + phoneNumber;
	}
}
