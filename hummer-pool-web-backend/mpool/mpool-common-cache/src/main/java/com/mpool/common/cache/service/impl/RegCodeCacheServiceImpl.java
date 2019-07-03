package com.mpool.common.cache.service.impl;

import java.util.concurrent.TimeUnit;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.mpool.common.cache.service.RegCodeCacheService;

@Service
public class RegCodeCacheServiceImpl implements RegCodeCacheService {

	@Autowired
	private RedisTemplate redisTemplate;

	private final String PREFIX = "MAIL_";

	@Override
	public String getCode(String key, String mail) {
		Object object = redisTemplate.opsForValue().get(PREFIX + key + mail);
		if (object != null) {
			return object.toString();
		} else {
			return null;
		}
	}

	@Override
	public void saveCode(String key, String mail, String value, int second) {
		redisTemplate.opsForValue().set(PREFIX + key + mail, value, second, TimeUnit.SECONDS);
	}

	@Override
	public boolean checkCode(String key, String mail, String captchaCode) {
		boolean flag = false;
		if (!"".equals(captchaCode) && captchaCode != null)
			flag = captchaCode.equals(getCode(key, mail));
		return flag;
	}

}
