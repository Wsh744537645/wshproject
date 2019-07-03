package com.mpool.admin.module.alarm.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mpool.admin.module.alarm.service.AlarmSettingService;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.common.Result;
import com.mpool.common.annotation.CurrentUser;
import com.mpool.common.model.admin.SysAlarmSetting;
import com.mpool.common.model.admin.SysUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = { "/alarm", "/apis/alarm" })
@Api("报警接口")
public class AlarmSettingController {
	@Autowired
	private AlarmSettingService alarmSettingService;

	@GetMapping("/list")
	@ApiOperation("获得模板列表")
	public Result getList() {
		List<SysAlarmSetting> list = alarmSettingService.getList();
		return Result.ok(list);
	}

	@GetMapping("/info/{id}")
	public Result getInfo(@PathVariable("id") Integer id) {
		SysAlarmSetting result = alarmSettingService.getInfo(id);
		return Result.ok(result);
	}

	@PostMapping("/add")
	@ApiOperation("添加报警设置")
	public Result addTemplate(@Valid @RequestBody SysAlarmSetting alarmSetting) {
		Long userId = AdminSecurityUtils.getUser().getUserId();
		alarmSetting.setUserId(userId);
		alarmSettingService.addAlarmSetting(alarmSetting);
		return Result.ok();
	}

	@PostMapping("/deleteTemplate")
	@ApiOperation("删除模板")
	public Result deleteTemplate(Integer id) {
		alarmSettingService.deleteTemplate(id);
		return Result.ok();
	}

	@GetMapping("/setUserAlarm")
	@ApiOperation("设置用户告警")
	public Result setUserAlarm(Integer id, Long userId) {
		alarmSettingService.setUserAlarm(id, userId);
		return Result.ok();
	}

	@GetMapping("/deleteUserAlarm")
	@ApiOperation("删除该用户的告警")
	public Result deleteUserAlarm(Long userId) {
		alarmSettingService.deleteUserAlarm(userId);
		return Result.ok();
	}

	@GetMapping("/getAlarmUser/{id}")
	@ApiOperation("获取告警模板下面使用的用户")
	public Result getAlarmUser(@PathVariable(value = "id") Integer id) {
		List<Map<String, Object>> list = alarmSettingService.getAlarmUser(id);
		return Result.ok(list);
	}

	@GetMapping("/getAlarmUserSelect")
	@ApiOperation("获得 告警用户的下拉菜单")
	public Result getAlarmUserSelect() {
		List<Map<String, Object>> list = alarmSettingService.getAlarmUserSelect();
		return Result.ok(list);
	}
}
