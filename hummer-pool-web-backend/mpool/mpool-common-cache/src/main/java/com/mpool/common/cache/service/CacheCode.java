package com.mpool.common.cache.service;

public interface CacheCode {
	void putCode(String key, String value);

	String getCode(String key);

	String removeCode(String key);
}
