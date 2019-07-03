package com.mpool.admin.module.alarm.service;

import java.util.List;
import java.util.Map;

import com.mpool.admin.MpoolAdminApplication;
import com.mpool.admin.module.alarm.model.NotifyModel;
import com.mpool.common.model.account.Currency;
import com.mpool.common.model.admin.SysAlarmSetting;

public interface NotifyService {
	void notifyUser(List<SysAlarmSetting> alarmSettings, Currency currency);

	void sendNotify(final SysAlarmSetting alarmSetting, final Long userId, final Map<String, Object> param);
}
