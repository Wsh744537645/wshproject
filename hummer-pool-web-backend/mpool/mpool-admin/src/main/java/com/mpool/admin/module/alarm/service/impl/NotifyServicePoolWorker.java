package com.mpool.admin.module.alarm.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mpool.admin.mapperUtils.pool.MiningWorkersMapperUtils;
import com.mpool.admin.mapperUtils.pool.StatsWorkersHourMapperUtils;
import com.mpool.common.model.account.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpool.admin.module.alarm.service.NotifyServiceAbstract;
import com.mpool.admin.module.common.EmailService;
import com.mpool.admin.module.common.SmsService;
import com.mpool.admin.module.pool.mapper.MiningWorkersMapper;
import com.mpool.admin.module.pool.mapper.StatsWorkersHourMapper;
import com.mpool.admin.module.system.mapper.SysUserMapper;
import com.mpool.common.model.admin.SysAlarmSetting;
import com.mpool.common.util.DateUtil;

@Service("notifyServicePoolWorker")
public class NotifyServicePoolWorker extends NotifyServiceAbstract {

	private static final Logger log = LoggerFactory.getLogger(NotifyServicePoolWorker.class);

	public NotifyServicePoolWorker(RedisTemplate redisTemplate, EmailService emailService, SmsService smsService,
			SysUserMapper sysUserMapper, ObjectMapper objectMapper) {
		super(redisTemplate, emailService, smsService, sysUserMapper, objectMapper);
		this.setTemplateEmail("email/notifyPoolWorker.html");
		this.setTemplatePhone("SMS_152549262");
	}

	@Autowired
	private MiningWorkersMapperUtils miningWorkersMapper;
	@Autowired
	private StatsWorkersHourMapperUtils statsWorkersHourMapper;

	@Override
	public void notifyUser(List<SysAlarmSetting> alarmSettings, Currency currency) {
		Integer currentworker = miningWorkersMapper.getPoolWorkerActiveCountByCurrency(currency.getType());
		// Date date = new Date();
		// String hour = DateUtil.getYYYYMMddHH(DateUtil.addHour(date, -1));
		// Integer lastactiveWorker = statsWorkersHourMapper.getActiveWorker(hour);
		if (currentworker == null) {
			currentworker = 0;
		}
//		if (lastactiveWorker == null) {
//			lastactiveWorker = 0;
//		}

//		if (currentworker >= lastactiveWorker) {
//			log.debug("No need for early warning>>>>>>>>>>>>>>>> currentworker = {} , lastactiveWorker = {}",
//					currentworker, lastactiveWorker);
//			return;
//		}

		// double workerRate = ((double) lastactiveWorker - (double) currentworker) /
		// (double) lastactiveWorker;
		Map<String, Object> notifyModel = new HashMap<>();
		// log.debug(">>>>>>>>>>>>>>>current {} = ({} - {}) / {}", workerRate,
		// lastactiveWorker, currentworker,lastactiveWorker);
		
		// 一个周期只能发送一个
		notifyModel.put("name", "Pool");
		notifyModel.put("value", currentworker);
		// 连续重复不能超过3个

		for (SysAlarmSetting sysAlarmSetting : alarmSettings) {
			Integer workerNumber = sysAlarmSetting.getWorkerNumber();
			if (workerNumber != null && !workerNumber.equals(0)) {
				// 预警值小于当前值
				if (workerNumber > currentworker) {
					// 报警
				
					if (checkNotify(sysAlarmSetting.getUserId() + ":worker:" + "checkNotify", sysAlarmSetting)) {
						continue;
					}
					if (checkSendCount(notifyModel.hashCode() + "",
							sysAlarmSetting.getUserId() + ":worker:" + "checkSendCount")) {
						Long userId = sysAlarmSetting.getUserId();
						sendNotify(sysAlarmSetting, userId, notifyModel);
					}
				}
			}
		}
	}
}
