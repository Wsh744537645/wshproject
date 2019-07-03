package com.mpool.account.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.mpool.common.model.account.AlarmNotifyUser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mpool.account.service.AlarmSettingService;
import com.mpool.account.utils.AccountSecurityUtils;
import com.mpool.common.Result;
import com.mpool.common.model.account.AlarmSetting;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = { "/alarm", "/apis/alarm" })
@Api("报警接口")
public class AlarmSettingController {
	@Autowired
	private AlarmSettingService alarmSettingService;

	@GetMapping("/info")
	@ApiOperation("获取告警信息")
	public Result getInfo() {
		Long userId = AccountSecurityUtils.getUser().getUserId();
		Map<String, Object> result = alarmSettingService.getInfo(userId);
		return Result.ok(result);
	}

	@GetMapping("/info/{userId}")
	@ApiOperation("主账号:获取告警信息")
	@RequiresPermissions("master")
	public Result getInfoByUserId(@PathVariable("userId") Long userId) {
		Map<String, Object> result = alarmSettingService.getInfo(userId);
		return Result.ok(result);
	}

	@PostMapping("/add")
	@ApiOperation("添加报警设置")
	public Result addAlarmSetting(@Valid @RequestBody AlarmSetting alarmSetting,
			@RequestParam(value = "userId", required = false) Long userId) {
		if (userId == null) {
			userId = AccountSecurityUtils.getUser().getUserId();
		}
		alarmSetting.setUserId(userId);
		alarmSettingService.addAlarmSetting(alarmSetting);
		return Result.ok();
	}

	@PostMapping("/add/{userId}")
	@ApiOperation("主账号:添加报警设置")
	@RequiresPermissions("master")
	public Result addAlarmSettingByUserId(@Valid @RequestBody AlarmSetting alarmSetting,
			@PathVariable("userId") Long userId) {
		alarmSetting.setUserId(userId);
		alarmSettingService.addAlarmSetting(alarmSetting);
		return Result.ok();
	}

//	@GetMapping("/cancel/{userId}")
//	@ApiOperation("取消告警")
//	public Result cancel(@PathVariable(value = "userId") Long userId) {
//		AlarmSetting alarmSetting = new AlarmSetting();
//		alarmSetting.setUserId(userId);
//		alarmSetting.setIsEnable(false);
//		alarmSettingService.cancel(alarmSetting);
//		return Result.ok();
//	}
//
//	@GetMapping("/activation/{userId}")
//	@ApiOperation("激活告警")
//	public Result activation(@PathVariable(value = "userId") Long userId) {
//		AlarmSetting alarmSetting = new AlarmSetting();
//		alarmSetting.setUserId(userId);
//		alarmSetting.setIsEnable(true);
//		alarmSettingService.activation(alarmSetting);
//		return Result.ok();
//	}

	@GetMapping("/getSettingUserList")
	@ApiOperation("获取告警列表")
	public Result getSettingUserList(@RequestParam("userId") Long userId) {
		List<AlarmNotifyUser> result = alarmSettingService.settingUserList(userId);
		return Result.ok(result);
	}

	@DeleteMapping("/deleteSettingUserId")
	@ApiOperation("删除告警设置<单条&批量>参数类型是->Integer[] ids")
	public Result deleteSettingUserId(@RequestBody Integer[] ids) {
		alarmSettingService.deleteSettingUserId(Arrays.asList(ids));
		return Result.ok();
	}

	@PostMapping("/updateSettingUserId")
	@ApiOperation("修改报警设置->传参：json字符串id,phon,email")
	public Result updateSettingUserId(@RequestBody AlarmNotifyUser model) {
		alarmSettingService.updateSettingUserId(model);
		return Result.ok();
	}

	@PostMapping("/addAlarmSetting")
	@ApiOperation("添加报警设置")
//    @RequiresPermissions("master")
	public Result addAlarmSetting(@RequestBody AlarmNotifyUser model) {
		alarmSettingService.addAlarmSet(model);
		return Result.ok();
	}

	@GetMapping("/getAlarmSettingNotifyInfo")
	@ApiOperation("获取全部报警列表信息")
	public Result getAlarmSettingNotifyInfo() {
		List<AlarmSetting> result = alarmSettingService.getAlarmSettingNotify();
		return Result.ok(result);
	}
}
