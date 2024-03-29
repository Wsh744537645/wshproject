package com.mpool.common.cache.service.impl;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.mpool.common.cache.service.EmailCodeCacheService;

@Service
public class EmailCodeCacheServiceImpl implements EmailCodeCacheService {

	private static final Logger log = LoggerFactory.getLogger(EmailCodeCacheServiceImpl.class);

	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 验证码有效时间 秒
	 */
	private int second = 300;

	private final String PREFIX = "SHARE_EMAIL_";

	@Override
	public String getCode(String key) {

		Object object = redisTemplate.opsForValue().get(PREFIX + key);
		log.debug("CaptchaCacheService get -> {} ,value -> {}", PREFIX + key, object);
		if (object != null) {
			return object.toString();
		} else {
			return null;
		}

	}

	@Override
	public void putCode(String key, String value) {
		log.debug("EmailCodeCacheService set -> {}", PREFIX + key + ":" + value + ":" + second);
		redisTemplate.opsForValue().set(PREFIX + key, value, second, TimeUnit.SECONDS);
	}

	@Override
	public String removeCode(String key) {
		String code = getCode(key);
		if (code != null) {
			redisTemplate.delete(key);
		}
		return code;
	}

}
