package com.mpool.common.cache.service;

public interface EmailCodeCacheService extends CacheCode {
	default String builderKey(String sessionId, String email) {
		return sessionId + ":" + email;
	}
}
