package com.mpool.account.notify;

import java.util.List;

import com.mpool.common.model.account.AlarmSetting;

public interface NotifyUserShareService {
	void notifyUser(List<AlarmSetting> alarmSetting);
}
