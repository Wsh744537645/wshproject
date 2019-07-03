package com.mpool.account.config.shiro.cache;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class PasswordRetryCache implements Cache<String, AtomicInteger> {
	@Autowired
	private RedisTemplate redisTemplate;

	private final int DEFAULT_RETRY_TIME = 10;

	private int retryTime = DEFAULT_RETRY_TIME;

	private final String PREFIX = "PASSWORD_RETRY:";

//	public PasswordRetryCache(RedisTemplate redisTemplate) {
//		super();
//		this.redisTemplate = redisTemplate;
//	}
//
//	public PasswordRetryCache(RedisTemplate redisTemplate, int retryTime) {
//		super();
//		this.redisTemplate = redisTemplate;
//		this.retryTime = retryTime;
//	}
//
//	protected RedisTemplate getRedisTemplate() {
//		return redisTemplate;
//	}
//
//	protected void setRedisTemplate(RedisTemplate redisTemplate) {
//		this.redisTemplate = redisTemplate;
//	}

	protected int getRetryTime() {
		return retryTime;
	}

	protected void setRetryTime(int retryTime) {
		this.retryTime = retryTime;
	}

	@Override
	public AtomicInteger get(String key) throws CacheException {

		return (AtomicInteger) redisTemplate.opsForValue().get(PREFIX + key);
	}

	@Override
	public AtomicInteger put(String key, AtomicInteger value) throws CacheException {
		redisTemplate.opsForValue().set(PREFIX + key, value, retryTime, TimeUnit.MINUTES);
		return value;
	}

	@Override
	public AtomicInteger remove(String key) throws CacheException {
		AtomicInteger atomicInteger = get(PREFIX + key);
		return atomicInteger;
	}

	@Override
	public void clear() throws CacheException {
		redisTemplate.delete(redisTemplate.keys(PREFIX + "*"));
	}

	@Override
	public int size() {
		return redisTemplate.keys(PREFIX + "*").size();
	}

	@Override
	public Set<String> keys() {
		return redisTemplate.keys(PREFIX + "*");
	}

	@Override
	public Collection<AtomicInteger> values() {
		return redisTemplate.opsForValue().multiGet(redisTemplate.keys(PREFIX + "*"));
	}

}
