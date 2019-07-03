package com.mpool.admin.module.alarm.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.admin.SysUserAlarm;

@Mapper
public interface SysUserAlarmMapper extends BaseMapper<SysUserAlarm> {

	void deleteByAlarmId(@Param("alarmId") Integer id);

}
