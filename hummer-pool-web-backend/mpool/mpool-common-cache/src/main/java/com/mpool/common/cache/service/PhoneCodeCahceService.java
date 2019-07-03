package com.mpool.common.cache.service;

public interface PhoneCodeCahceService extends CacheCode {
	default String builderKey(String sessionId, String code) {
		return sessionId + ":" + code;
	}
}
