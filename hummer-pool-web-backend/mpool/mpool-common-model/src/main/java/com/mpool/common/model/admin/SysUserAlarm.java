package com.mpool.common.model.admin;

import static com.mpool.common.model.constant.AdminConstant.TABLE_PREFIX;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(TABLE_PREFIX + "user_alarm")
public class SysUserAlarm {
	@TableId(type = IdType.INPUT)
	private Long userId;
	private Integer alarmId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(Integer alarmId) {
		this.alarmId = alarmId;
	}
}
