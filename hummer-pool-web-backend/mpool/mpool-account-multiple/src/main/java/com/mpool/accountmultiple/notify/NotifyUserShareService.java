package com.mpool.accountmultiple.notify;

import com.mpool.common.model.account.AlarmSetting;

import java.util.List;

public interface NotifyUserShareService {
	void notifyUser(List<AlarmSetting> alarmSetting);
}
