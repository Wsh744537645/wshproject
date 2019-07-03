package com.mpool.admin.module.alarm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.mpool.common.model.account.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpool.admin.module.alarm.model.NotifyModel;
import com.mpool.admin.module.common.EmailService;
import com.mpool.admin.module.common.SmsService;
import com.mpool.admin.module.system.mapper.SysUserMapper;
import com.mpool.common.model.admin.SysAlarmSetting;
import com.mpool.common.model.admin.SysUser;

public abstract class NotifyServiceAbstract implements NotifyService {

	private static final Logger log = LoggerFactory.getLogger(NotifyServiceAbstract.class);

	private String templateEmail;

	private String templatePhone;

	@SuppressWarnings("rawtypes")
	private RedisTemplate redisTemplate;

	private EmailService emailService;

	private SmsService smsService;

	private SysUserMapper sysUserMapper;

	private ObjectMapper objectMapper;

	@Override
	public abstract void notifyUser(List<SysAlarmSetting> alarmSettings, Currency currency);

	public NotifyServiceAbstract(RedisTemplate redisTemplate, EmailService emailService, SmsService smsService,
			SysUserMapper sysUserMapper, ObjectMapper objectMapper) {
		super();
		this.redisTemplate = redisTemplate;
		this.emailService = emailService;
		this.smsService = smsService;
		this.sysUserMapper = sysUserMapper;
		this.objectMapper = objectMapper;
	}

	public void setTemplateEmail(String templateEmail) {
		this.templateEmail = templateEmail;
	}

	public void setTemplatePhone(String templatePhone) {
		this.templatePhone = templatePhone;
	}

	/**
	 * 判断一个key在一个周期内没有发送
	 * 
	 * @param key
	 * @param p
	 * @return false 没有发送过 true 已经发送过
	 */
	protected boolean checkNotify(String key, SysAlarmSetting sysAlasrmSetting) {
		if (redisTemplate.opsForValue().get(key) != null) {
			log.debug("key => {} Notification has been sent", key);
			return true;
		}
		redisTemplate.opsForValue().set(key, "", sysAlasrmSetting.getCycle(), TimeUnit.SECONDS);
		return false;
	}

	/**
	 * 检测一个数据是否发生了3次
	 * 
	 * @param hashCode
	 * @param key
	 * @return true 没有超过3次 false 超过3次 不能发送
	 */
	@SuppressWarnings("unchecked")
	protected boolean checkSendCount(String hashCode, String key) {
		boolean flg = true;
		@SuppressWarnings("rawtypes")
		Map entries = redisTemplate.opsForHash().entries(key);
		log.debug("entries is empty hashCode ={} , key ={}", hashCode, key);
		if (entries == null) {
			redisTemplate.opsForHash().put(key, hashCode, "1");
		} else {
			log.debug("entries values in {}", entries);
			Object object = entries.get(hashCode);

			if (object == null) {
				for (Object hash : entries.keySet()) {
					redisTemplate.opsForHash().delete(key, hash);
				}
				redisTemplate.opsForHash().put(key, hashCode, "1");
			} else {
				int a = Integer.parseInt(object.toString());
				if (a > 3) {
					flg = false;
				} else {
					a++;
					redisTemplate.opsForHash().put(key, hashCode, "" + a);
				}
			}
		}
		return flg;
	}

	@Override
	public void sendNotify(final SysAlarmSetting alarmSetting, final Long userId, final Map<String, Object> param) {
		log.debug("param => {}", param);
		SysUser user = sysUserMapper.selectById(userId);
		if (alarmSetting.getEmail() != null && alarmSetting.getEmail()) {
			// 邮件通知
			String userEmail = user.getEmail();
			if (!StringUtils.isEmpty(userEmail)) {
				// 发送内容
				IContext context;
				context = new Context(Locale.getDefault(), param);
				// 发送邮件
				emailService.sendAlarmEmail(userEmail, templateEmail, "告警", context);
			}

		}
		String telphone = user.getTelphone();
		if (!StringUtils.isEmpty(telphone)) {
			if (alarmSetting.getPhone() != null && alarmSetting.getPhone()) {
				try {
					smsService.sendAlarmSms(telphone, templatePhone, objectMapper.writeValueAsString(param));
				} catch (JsonProcessingException e) {
					log.error("JsonProcessingException", e);
				}
			}
		}

	}

	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

}
