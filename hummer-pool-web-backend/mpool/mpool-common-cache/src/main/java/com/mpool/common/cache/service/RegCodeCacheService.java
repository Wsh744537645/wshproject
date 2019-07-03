package com.mpool.common.cache.service;

public interface RegCodeCacheService {
	String getCode(String key ,String mail);

	/**
	 * @param key
	 * @param value
	 * @param second 过期时间
	 */
	void saveCode(String key, String mail, String value, int second);

	/**
	 * @param code
	 * @return ture 成功 false 失败
	 */
	boolean checkCode(String key, String mail,String captchaCode);
}
