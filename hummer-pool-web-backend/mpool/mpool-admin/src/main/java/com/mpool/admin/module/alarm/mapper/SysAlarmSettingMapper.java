package com.mpool.admin.module.alarm.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.admin.SysAlarmSetting;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysAlarmSettingMapper extends BaseMapper<SysAlarmSetting> {

	List<SysAlarmSetting> getAlarmSettingNotify(@Param("currencyId") Integer currencyId);

	/**
	 * @param id
	 * @return
	 */
	List<Map<String, Object>> getAlarmUser(Integer id);

	List<Map<String, Object>> getAlarmUserSelect();

	List<SysAlarmSetting> getModifyPasswordNotifyUser(@Param("currencyId") Integer currencyId);

	List<SysAlarmSetting> getCreateBillNotifyUser(@Param("currencyId") Integer currencyId);

	@Select("select * from admin_alarm_setting where currency_id=#{currencyId}")
	List<SysAlarmSetting> getAllList(@Param("currencyId") Integer currencyId);

}
