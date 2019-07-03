package com.mpool.accountmultiple.service;

import com.mpool.common.model.account.AlarmNotifyUser;
import com.mpool.common.model.account.AlarmSetting;

import java.util.List;
import java.util.Map;

public interface AlarmSettingService {

	void addAlarmSetting(AlarmSetting alarmSetting);

	void cancel(AlarmSetting alarmSetting);

	void activation(AlarmSetting alarmSetting);

	/**
	 * 获取需要被通知的 用户设置
	 * 
	 * @return
	 */
	List<AlarmSetting> getAlarmSettingNotify();

	Map<String, Object> getInfo(Long userId);

	/**
	 * wgg
	 * 获取告警列表
	 * @param userId
	 * @return
	 */
	List<AlarmNotifyUser> settingUserList(Long userId);

	/**
	 * wgg
	 * 删除报警设置
	 * @param ids
	 */
	void deleteSettingUserId(List<Integer> ids);

	/**
	 * wgg
	 * 修改用户手机邮箱
	 * @param model
	 */
	void updateSettingUserId(AlarmNotifyUser model);

	/**
	 * wgg
	 * 添加报警设置
	 * @param alarmSetting
	 */
	void addAlarmSet(AlarmNotifyUser alarmSetting);
}
