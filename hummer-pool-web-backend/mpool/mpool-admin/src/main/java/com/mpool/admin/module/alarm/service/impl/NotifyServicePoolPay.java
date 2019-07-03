package com.mpool.admin.module.alarm.service.impl;

import java.util.List;

import com.mpool.common.model.account.Currency;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpool.admin.module.alarm.model.NotifyModel;
import com.mpool.admin.module.alarm.service.NotifyService;
import com.mpool.admin.module.alarm.service.NotifyServiceAbstract;
import com.mpool.admin.module.common.EmailService;
import com.mpool.admin.module.common.SmsService;
import com.mpool.admin.module.system.mapper.SysUserMapper;
import com.mpool.common.model.admin.SysAlarmSetting;

@Service("notifyServicePoolPay")
public class NotifyServicePoolPay extends NotifyServiceAbstract {

	public NotifyServicePoolPay(RedisTemplate redisTemplate, EmailService emailService, SmsService smsService,
			SysUserMapper sysUserMapper, ObjectMapper objectMapper) {
		super(redisTemplate, emailService, smsService, sysUserMapper, objectMapper);
		this.setTemplateEmail("email/notifyPoolPay.html");
		this.setTemplatePhone("SMS_152851697");
	}

	@Override
	public void notifyUser(List<SysAlarmSetting> alarmSettings, Currency currency) {

	}

}
