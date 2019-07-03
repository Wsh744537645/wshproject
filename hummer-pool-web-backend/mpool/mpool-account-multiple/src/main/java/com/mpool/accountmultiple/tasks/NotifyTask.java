package com.mpool.accountmultiple.tasks;

import com.mpool.accountmultiple.notify.NotifyUserShareService;
import com.mpool.accountmultiple.notify.NotifyUserWorkerService;
import com.mpool.accountmultiple.service.AlarmSettingService;
import com.mpool.common.model.account.AlarmSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotifyTask {

	private static final Logger log = LoggerFactory.getLogger(NotifyTask.class);

	@Autowired
	private AlarmSettingService alarmSettingService;
	@Autowired
	private NotifyUserShareService notifyUserShareService;

	@Autowired
	private NotifyUserWorkerService notifyUserWorkerService;
	
	private volatile boolean lock = true;

	@Scheduled(cron = "0 */1 * * * ?")
	public void alarmNotify() {
		log.debug("runing alarmNotify lock => {}", lock);

		if (lock) {
			lock = false;
			try {
				List<AlarmSetting> list = alarmSettingService.getAlarmSettingNotify();
				notifyUserShareService.notifyUser(list);
				notifyUserWorkerService.notifyUser(list);
			} catch (Exception e) {
				log.error("alarmNotify error", e);
			}
			lock = true;
		}
		log.debug("end alarmNotify lock => {}", lock);
	}
}
