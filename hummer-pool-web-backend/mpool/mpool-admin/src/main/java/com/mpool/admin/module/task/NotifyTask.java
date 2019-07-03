package com.mpool.admin.module.task;

import java.util.List;

import com.mpool.admin.module.system.service.CurrencyService;
import com.mpool.common.model.account.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mpool.admin.module.alarm.mapper.SysAlarmSettingMapper;
import com.mpool.admin.module.alarm.service.AlarmSettingService;
import com.mpool.admin.module.alarm.service.NotifyService;
import com.mpool.common.model.account.AlarmSetting;
import com.mpool.common.model.admin.SysAlarmSetting;

/**
 * 每天自动添加待支付项
 * 
 * @author chenjian2
 *
 */
@Component
public class NotifyTask {

	private static final Logger log = LoggerFactory.getLogger(NotifyTask.class);

	private volatile boolean lock = true;
	@Autowired
	@Qualifier("notifyServicePoolShare")
	private NotifyService notifyServicePoolShare;
	@Autowired
	@Qualifier("notifyServicePoolWorker")
	private NotifyService notifyServicePoolWorker;
	@Autowired
	@Qualifier("notifyServicePoolBlock")
	private NotifyService notifyServicePoolBlock;
	@Autowired
	private AlarmSettingService alarmSettingService;
	@Autowired
	private CurrencyService currencyService;

	@Scheduled(cron = "0 */2 * * * ?")
	public void alarmNotify() {
		log.debug("runing alarmNotify lock => {}", lock);

		if (lock) {
			lock = false;
			try {
				List<Currency> currencyList = currencyService.selectListByEnable();
				for(Currency currency : currencyList) {
					List<SysAlarmSetting> list = alarmSettingService.getAlarmSettingNotify(currency.getId());
					notifyServicePoolShare.notifyUser(list, currency);
					notifyServicePoolWorker.notifyUser(list, currency);
					notifyServicePoolBlock.notifyUser(list, currency);
				}
			} catch (Exception e) {
				log.error("alarmNotify error", e);
			}
			lock = true;
		}
		log.debug("end alarmNotify lock => {}", lock);
	}
}
