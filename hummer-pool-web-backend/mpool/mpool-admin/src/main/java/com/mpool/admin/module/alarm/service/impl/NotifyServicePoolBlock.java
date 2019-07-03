package com.mpool.admin.module.alarm.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpool.admin.mapperUtils.account.FoundBlocksMapperUtils;
import com.mpool.admin.module.alarm.service.NotifyServiceAbstract;
import com.mpool.admin.module.common.EmailService;
import com.mpool.admin.module.common.SmsService;
import com.mpool.admin.module.system.mapper.SysUserMapper;
import com.mpool.common.model.account.Currency;
import com.mpool.common.model.admin.SysAlarmSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("notifyServicePoolBlock")
public class NotifyServicePoolBlock extends NotifyServiceAbstract {

	private static final Logger log = LoggerFactory.getLogger(NotifyServicePoolBlock.class);

	public NotifyServicePoolBlock(RedisTemplate redisTemplate, EmailService emailService, SmsService smsService,
								  SysUserMapper sysUserMapper, ObjectMapper objectMapper) {
		super(redisTemplate, emailService, smsService, sysUserMapper, objectMapper);
		this.setTemplateEmail("email/notifyPoolBlocks.html");
		this.setTemplatePhone("SMS_152549262");
	}

	@Autowired
	private FoundBlocksMapperUtils foundBlocksMapperUtils;

	@Override
	public void notifyUser(List<SysAlarmSetting> alarmSettings, Currency currency) {
		Integer block = foundBlocksMapperUtils.getBlockBycurrency(currency.getType());
		if (block == null) {
			block = 0;
		}
		Object object = getRedisTemplate().opsForValue().get("pool:blocks");
		int size = 0;
		if (object != null) {
			size = Integer.parseInt(object.toString());
		} else {
			getRedisTemplate().opsForValue().set("pool:blocks", block);
			return;
		}

		Map<String, Object> notifyModel = new HashMap<>();
		notifyModel.put("name", "Pool");
		notifyModel.put("value", "爆块了");
		for (SysAlarmSetting sysAlarmSetting : alarmSettings) {
			// 预警值小于当前值
			if (sysAlarmSetting.getBlock() != null && sysAlarmSetting.getBlock()) {
				if (size != block && size < block) {
					Long userId = sysAlarmSetting.getUserId();
					sendNotify(sysAlarmSetting, userId, notifyModel);
					getRedisTemplate().opsForValue().set("pool:blocks", block);
				}
			}
		}
	}
}
