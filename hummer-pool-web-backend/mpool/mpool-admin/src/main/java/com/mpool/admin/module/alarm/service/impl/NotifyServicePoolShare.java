package com.mpool.admin.module.alarm.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mpool.admin.mapperUtils.pool.StatsPoolHourMapperUtils;
import com.mpool.common.model.account.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpool.admin.module.alarm.model.NotifyModel;
import com.mpool.admin.module.alarm.service.NotifyService;
import com.mpool.admin.module.alarm.service.NotifyServiceAbstract;
import com.mpool.admin.module.common.EmailService;
import com.mpool.admin.module.common.SmsService;
import com.mpool.admin.module.dashboard.service.PoolApiService;
import com.mpool.admin.module.pool.mapper.MiningWorkersMapper;
import com.mpool.admin.module.pool.mapper.StatsPoolHourMapper;
import com.mpool.admin.module.system.mapper.SysUserMapper;
import com.mpool.common.model.admin.SysAlarmSetting;
import com.mpool.common.model.pool.MiningWorkers;
import com.mpool.common.model.pool.StatsPoolHour;
import com.mpool.common.util.DateUtil;

@Service("notifyServicePoolShare")
public class NotifyServicePoolShare extends NotifyServiceAbstract {

	private static final Logger log = LoggerFactory.getLogger(NotifyServicePoolShare.class);

	public NotifyServicePoolShare(RedisTemplate redisTemplate, EmailService emailService, SmsService smsService,
			SysUserMapper sysUserMapper, ObjectMapper objectMapper) {
		super(redisTemplate, emailService, smsService, sysUserMapper, objectMapper);
		this.setTemplateEmail("email/notifyPoolShare.html");
		this.setTemplatePhone("SMS_152544365");
	}

	@Autowired
	private PoolApiService poolApiService;
	@Autowired
	private StatsPoolHourMapperUtils statsPoolHourMapper;

	@Override
	public void notifyUser(List<SysAlarmSetting> alarmSettings, Currency currency) {
		MiningWorkers miningWorkers = poolApiService.getCurrentShare();
		Double currentShare = miningWorkers.getHashAccept15mT();
		Date date = new Date();
		String hour = DateUtil.getYYYYMMddHH(DateUtil.addHour(date, -1));
		StatsPoolHour statsPoolHourByHour = statsPoolHourMapper.getStatsPoolHourByHourByCurrency(hour, currency.getType());
		double last1HAcceptT = 0;
		if (statsPoolHourByHour != null) {
			last1HAcceptT = statsPoolHourByHour.getHashAcceptT();
		} else {
			log.debug("Past 1 hours pool share null");
		}

		// 当前算力大于过去1小时算力不需要预警
		if (currentShare >= last1HAcceptT) {
			log.debug("No need for early warning>>>>>>>>>>>>>>>> currentShare = {} , last1HShare = {}", currentShare,
					last1HAcceptT);
			return;
		}
		double shareRate = (last1HAcceptT - currentShare) / last1HAcceptT;
		Map<String, Object> notifyModel = new HashMap<>();
		notifyModel.put("name", "Pool");
		notifyModel.put("value", currentShare);
		notifyModel.put("pre", last1HAcceptT);

		// 一个周期只能发送一个

		for (SysAlarmSetting sysAlarmSetting : alarmSettings) {
			Double shareR = sysAlarmSetting.getShareRate();
			if (shareR != null && !shareR.equals(0d)) {
				// 预警值小于当前值
				if (shareR < shareRate) {
					// 报警
					if (checkNotify(sysAlarmSetting.getUserId() + ":share:" + "checkNotify", sysAlarmSetting)) {
						// 当前周期中已经发送
						continue;
					}

					log.debug("current will do warning>>>>>>>>>>>>>>>>currentShare = {} , last1HShare = {}, shareRate = {}， dbrate = {}, userId= {}", currentShare,
							last1HAcceptT, shareRate, shareR, sysAlarmSetting.getUserId());

					if (checkSendCount(notifyModel.hashCode() + "",
							sysAlarmSetting.getUserId() + ":share:" + "checkSendCount")) {
						Long userId = sysAlarmSetting.getUserId();
						sendNotify(sysAlarmSetting, userId, notifyModel);
					}
				}
			}
		}

	}

}
