package com.mpool.admin.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpool.admin.module.account.mapper.UserMapper;
import com.mpool.admin.module.alarm.service.AlarmSettingService;
import com.mpool.admin.module.common.EmailService;
import com.mpool.admin.module.common.SmsService;
import com.mpool.admin.module.system.mapper.SysUserMapper;
import com.mpool.common.model.account.User;
import com.mpool.common.model.admin.SysAlarmSetting;
import com.mpool.common.model.admin.SysUser;

@Component
public class NotifyUtils {

	private static final Logger log = LoggerFactory.getLogger(NotifyUtils.class);
	@Autowired
	private ExecutorService executorService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private SmsService smsService;
	@Autowired
	private SysUserMapper sysUserMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private AlarmSettingService alarmSettingService;

	public void modifyPasswordNotifyAdmin(String adminUserId, String accountUsername) {

		executorService.execute(() -> {
			SysUser user = sysUserMapper.selectById(adminUserId);
			Map<String, Object> param = new HashMap<>();
			param.put("admin_account", user.getUsername());
			param.put("user_account", accountUsername);

			List<SysAlarmSetting> users = alarmSettingService.getModifyPasswordNotifyUser();
			for (SysAlarmSetting sysAlarmSetting : users) {
				SysUser notifyUser = sysUserMapper.selectById(sysAlarmSetting.getUserId());
				if (!StringUtils.isEmpty(notifyUser.getEmail()) && sysAlarmSetting.getEmail()) {
					IContext context = new Context(Locale.getDefault(), param);
					emailService.sendAlarmEmail(notifyUser.getEmail(), "notify/resetPasswordNotifyAdmin.html", "系统通知",
							context);
				}
				if (!StringUtils.isEmpty(notifyUser.getTelphone()) && sysAlarmSetting.getPhone()) {
					String paramStr;
					try {
						paramStr = objectMapper.writeValueAsString(param);
					} catch (JsonProcessingException e) {
						log.error("", e);
						return;
					}
					smsService.sendAlarmSms(notifyUser.getTelphone(), "SMS_158900119", paramStr);
				}
			}

		});
	}

	public void modifyPasswordNotifyAccount(String accountUserId, String password) {
		executorService.execute(() -> {
			Map<String, Object> map = new HashMap<>();
			map.put("password",password);
			log.debug("accountUserId => {}", accountUserId);
			log.debug("newPassword => {}", password);
			User user = userMapper.selectById(accountUserId);
			if (!StringUtils.isEmpty(user.getUserPhone())) {
				String param;
				try {
					param = objectMapper.writeValueAsString(map);
				} catch (JsonProcessingException e) {
					log.error("", e);
					return;
				}
				String userPhone = user.getUserPhone();
				log.debug("userPhone =>{}",userPhone);
				smsService.sendAlarmSms(userPhone, "SMS_158905090", param);
			}
		});
	}

	public void createBillNotify(String adminUserId) {
		executorService.execute(() -> {
			SysUser user = sysUserMapper.selectById(adminUserId);
			Map<String, Object> param = new HashMap<>();
			param.put("admin_account", user.getUsername());

			List<SysAlarmSetting> notifyUsers = alarmSettingService.getCreateBillNotifyUser();
			for (SysAlarmSetting sysAlarmSetting : notifyUsers) {
				SysUser notifyUser = sysUserMapper.selectById(sysAlarmSetting.getUserId());
				if (!StringUtils.isEmpty(notifyUser.getEmail()) && sysAlarmSetting.getEmail()) {
					IContext context = new Context(Locale.getDefault(), param);
					emailService.sendAlarmEmail(notifyUser.getEmail(), "notify/createBillNotify.html", "系统通知", context);
				}
				if (!StringUtils.isEmpty(notifyUser.getTelphone()) && sysAlarmSetting.getPhone()) {
					String paramStr;
					try {
						paramStr = objectMapper.writeValueAsString(param);
					} catch (JsonProcessingException e) {
						log.error("", e);
						return;
					}
					smsService.sendAlarmSms(notifyUser.getTelphone(), "SMS_158900121", paramStr);
				}

			}

		});
	}
}
