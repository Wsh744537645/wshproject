package com.mpool.admin.module.alarm.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.mpool.common.model.admin.SysAlarmSetting;

public interface AlarmSettingService {

	SysAlarmSetting getInfo(Integer id);

	void addAlarmSetting(@Valid SysAlarmSetting alarmSetting);

	/**
	 * 获取需要被通知的 用户设置
	 * 
	 * @return
	 */
	List<SysAlarmSetting> getAlarmSettingNotify(Integer currencyId);

	List<SysAlarmSetting> getList();

	/**
	 * 设置用户告警
	 * 
	 * @param id
	 * @param userId
	 */
	void setUserAlarm(Integer id, Long userId);

	/**
	 * 获得模板使用的用户
	 * 
	 * @param id
	 * @return
	 */
	List<Map<String, Object>> getAlarmUser(Integer id);

	List<Map<String, Object>> getAlarmUserSelect();

	/**
	 * 删除用户告警
	 * 
	 * @param userId
	 */
	void deleteUserAlarm(Long userId);

	/**
	 * 删除模板
	 * 
	 * @param id
	 */
	void deleteTemplate(Integer id);

	/**
	 * 获得修改用户密码需要被通知的人
	 * 
	 * @return
	 */
	List<SysAlarmSetting> getModifyPasswordNotifyUser();

	/**
	 * 获取生成支付单需要被通知的人
	 * 
	 * @return
	 */
	List<SysAlarmSetting> getCreateBillNotifyUser();
}
