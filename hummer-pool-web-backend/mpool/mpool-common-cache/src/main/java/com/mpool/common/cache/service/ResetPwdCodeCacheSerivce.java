package com.mpool.common.cache.service;

public interface ResetPwdCodeCacheSerivce {
	String getCode(String key, String mail);

	/**
	 * @param key
	 * @param value
	 * @param code
	 * @param second 过期时间
	 */
	void saveCode(String key, String mail, String value, int second);

	/**
	 * @param code
	 * @return ture 成功 false 失败
	 */
	boolean checkCode(String key, String mail,String code);
}
