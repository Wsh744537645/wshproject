package com.mpool.share.config.shiro.cache;

import com.mpool.common.util.RedisKeys;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Configuration
public class RedisShiroSessionDAO extends EnterpriseCacheSessionDAO {

	private static final Logger log = LoggerFactory.getLogger(RedisShiroSessionDAO.class);

	@Autowired
	private RedisTemplate redisTemplate;

	// 创建session
	@Override
	protected Serializable doCreate(Session session) {

		Serializable sessionId = super.doCreate(session);
		log.debug("------> create session -> {}", sessionId);
		final String key = RedisKeys.getShiroSessionKey(sessionId.toString());
		setShiroSession(key, session);
		return sessionId;
	}

	// 获取session
	@Override
	protected Session doReadSession(Serializable sessionId) {
		log.debug("------> read session -> {}", sessionId);
		Session session = super.doReadSession(sessionId);
		if (session == null) {
			final String key = RedisKeys.getShiroSessionKey(sessionId.toString());
			session = getShiroSession(key);
		}
		return session;
	}

	// 更新session
	@Override
	protected void doUpdate(Session session) {
		log.debug("------> update session -> {}", session.getId());
		super.doUpdate(session);
		final String key = RedisKeys.getShiroSessionKey(session.getId().toString());
		setShiroSession(key, session);
	}

	// 删除session
	@Override
	protected void doDelete(Session session) {
		log.debug("------> delete session -> {}", session.getId());
		super.doDelete(session);
		final String key = RedisKeys.getShiroSessionKey(session.getId().toString());
		redisTemplate.delete(key);
	}

	private Session getShiroSession(String key) {
		return (Session) redisTemplate.opsForValue().get(key);
	}

	private void setShiroSession(String key, Session session) {
		log.debug("------------------------------------------set shiro session -> {}", session.getTimeout());
		log.debug("------------------------------------------set shiro last access time -> {}",
				session.getLastAccessTime());
		log.debug("------------------------------------------set shiro start timestamp -> {}",
				session.getStartTimestamp());
		redisTemplate.opsForValue().set(key, session, 30, TimeUnit.MINUTES);
	}
}
