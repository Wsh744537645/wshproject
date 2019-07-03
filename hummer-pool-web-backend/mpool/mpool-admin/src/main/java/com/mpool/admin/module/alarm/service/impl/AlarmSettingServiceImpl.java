package com.mpool.admin.module.alarm.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.mpool.admin.module.system.service.CurrencyService;
import org.codehaus.groovy.transform.trait.Traits.TraitBridge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mpool.admin.module.alarm.mapper.SysAlarmSettingMapper;
import com.mpool.admin.module.alarm.mapper.SysUserAlarmMapper;
import com.mpool.admin.module.alarm.service.AlarmSettingService;
import com.mpool.admin.module.system.mapper.SysUserMapper;
import com.mpool.common.model.admin.SysAlarmSetting;
import com.mpool.common.model.admin.SysUserAlarm;

@Service
public class AlarmSettingServiceImpl implements AlarmSettingService {
	@Autowired
	private SysUserMapper sysUserMapper;
	@Autowired
	private SysAlarmSettingMapper sysAlarmSettingMapper;
	@Autowired
	private SysUserAlarmMapper sysUserAlarmMapper;
	@Autowired
	private CurrencyService currencyService;

	@Override
	public SysAlarmSetting getInfo(Integer id) {
		SysAlarmSetting alarmSetting = sysAlarmSettingMapper.selectById(id);
		return alarmSetting;
	}

	@Override
	public void addAlarmSetting(@Valid SysAlarmSetting alarmSetting) {
		SysAlarmSetting alarm = null;
		if (alarmSetting.getId() != null) {
			alarm = sysAlarmSettingMapper.selectById(alarmSetting.getId());
		}
		if (alarm == null) {
			alarmSetting.setCreateTime(new Date());
			sysAlarmSettingMapper.insert(alarmSetting);
		} else {
			alarmSetting.setUpdateTime(new Date());
			sysAlarmSettingMapper.updateById(alarmSetting);
		}
	}

	@Override
	public List<SysAlarmSetting> getAlarmSettingNotify(Integer currencyId) {
		List<SysAlarmSetting> selectList = sysAlarmSettingMapper.getAlarmSettingNotify(currencyId);
		return selectList;
	}

	@Override
	public List<SysAlarmSetting> getList() {
		List<SysAlarmSetting> list = sysAlarmSettingMapper.getAllList(currencyService.getCurCurrencyId());
		return list;
	}

	@Override
	public void setUserAlarm(Integer id, Long userId) {
		SysUserAlarm entity = new SysUserAlarm();
		entity.setAlarmId(id);
		entity.setUserId(userId);
		SysUserAlarm selectById = sysUserAlarmMapper.selectById(userId);
		if (selectById != null) {
			sysUserAlarmMapper.updateById(entity);
		} else {
			sysUserAlarmMapper.insert(entity);
		}

	}

	@Override
	public List<Map<String, Object>> getAlarmUser(Integer id) {

		return sysAlarmSettingMapper.getAlarmUser(id);
	}

	@Override
	public List<Map<String, Object>> getAlarmUserSelect() {
		return sysAlarmSettingMapper.getAlarmUserSelect();
	}

	@Override
	public void deleteUserAlarm(Long userId) {
		sysUserAlarmMapper.deleteById(userId);
	}

	@Override
	@Transactional
	public void deleteTemplate(Integer id) {
		sysUserAlarmMapper.deleteByAlarmId(id);
		sysAlarmSettingMapper.deleteById(id);
	}

	@Override
	public List<SysAlarmSetting> getModifyPasswordNotifyUser() {
		return sysAlarmSettingMapper.getModifyPasswordNotifyUser(currencyService.getCurCurrencyId());
	}

	@Override
	public List<SysAlarmSetting> getCreateBillNotifyUser() {
		return sysAlarmSettingMapper.getCreateBillNotifyUser(currencyService.getCurCurrencyId());
	}

}
