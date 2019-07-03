package com.mpool.accountmultiple.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpool.accountmultiple.mapper.AlarmNotifyUserMapper;
import com.mpool.accountmultiple.mapper.AlarmSettingMapper;
import com.mpool.accountmultiple.mapper.UserMapper;
import com.mpool.accountmultiple.service.AlarmSettingService;
import com.mpool.common.model.account.AlarmNotifyUser;
import com.mpool.common.model.account.AlarmSetting;
import com.mpool.common.model.account.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlarmSettingServiceImpl implements AlarmSettingService {

	private static final Logger log = LoggerFactory.getLogger(AlarmSettingServiceImpl.class);

	@Autowired
	private AlarmSettingMapper alarmSettingMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private AlarmNotifyUserMapper alarmNotifyUserMapper;

	@Override
	@Transactional
	public void addAlarmSetting(AlarmSetting alarmSetting) {
		Long userId = alarmSetting.getUserId();
		AlarmSetting alarm = alarmSettingMapper.selectById(userId);
		List<AlarmNotifyUser> notifyUser = alarmSetting.getNotifyUser();
		if (alarm == null) {
			alarmSetting.setCreateTime(new Date());
			alarmSettingMapper.insert(alarmSetting);
		} else {
			alarmSetting.setUpdateTime(new Date());
			alarmSettingMapper.updateById(alarmSetting);
		}

		if (notifyUser != null && notifyUser.size() != 0) {
			AlarmNotifyUser alarmNotifyUser = new AlarmNotifyUser();
			alarmNotifyUser.setUserId(userId);
			QueryWrapper<AlarmNotifyUser> queryWrapper = new QueryWrapper<AlarmNotifyUser>(alarmNotifyUser);

			alarmNotifyUserMapper.delete(queryWrapper);

			alarmNotifyUserMapper.insets(notifyUser, userId);
		}

	}

	@Override
	public void cancel(AlarmSetting alarmSetting) {
		alarmSetting.setUpdateTime(new Date());
		alarmSettingMapper.updateById(alarmSetting);
	}

	@Override
	public void activation(AlarmSetting alarmSetting) {
		alarmSetting.setUpdateTime(new Date());
		alarmSettingMapper.updateById(alarmSetting);
	}

	@Override
	public List<AlarmSetting> getAlarmSettingNotify() {
		AlarmSetting entity = new AlarmSetting();
		List<AlarmSetting> selectList = alarmSettingMapper.selectList(new QueryWrapper<AlarmSetting>(entity));
		return selectList;
	}

	@Override
	public Map<String, Object> getInfo(Long userId) {
		User user = userMapper.selectById(userId);
		if (user == null) {
			throw AccountException.userNotfound();
		}
		AlarmSetting alarmSetting = alarmSettingMapper.selectById(userId);
		if (alarmSetting == null) {
			alarmSetting = new AlarmSetting();
		}
		AlarmNotifyUser alarmNotifyUser = new AlarmNotifyUser();
		alarmNotifyUser.setUserId(userId);
		List<AlarmNotifyUser> alarmNotifyUserList = alarmNotifyUserMapper
				.selectList(new QueryWrapper<>(alarmNotifyUser));
		Map<String, Object> map = new HashMap<>();
		alarmSetting.setNotifyUser(alarmNotifyUserList);
		map.put("alarmSetting", alarmSetting);
		map.put("alarmNotifyUserList", alarmNotifyUserList);
		return map;
	}

	@Override
	public List<AlarmNotifyUser> settingUserList(Long userId) {
		if (userId != null) {
			List<AlarmNotifyUser> list = alarmNotifyUserMapper.settingUserList(userId);
			return list;
		}
		return null;
	}

	@Override
	public void deleteSettingUserId(List<Integer> ids) {
		alarmNotifyUserMapper.deleteSettingUserIds(ids);
	}

	@Override
	public void updateSettingUserId(AlarmNotifyUser model) {
//		修改 根据Id 修改 手机号码 邮箱  需要验证手机号码的格式和邮箱的格式
		if (model.getId() != null) {
			// 校验手机号码
			String telRegex = "[1][3456789]\\d{9}";
			if (model.getPhone().matches(telRegex) == true) {
				model.setPhone(model.getPhone());
			} else {
				throw new RuntimeException("请输入正确手机号码！");
			}
			// 校验邮箱格式
			String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
			if (model.getEmail().matches(str)) {
				model.setEmail(model.getEmail());
			} else {
				throw new RuntimeException("请输入正确邮箱格式！");
			}
			model.setUpdateTime(new Date());
			alarmNotifyUserMapper.updateById(model);
		}
	}

	@Override
	public void addAlarmSet(AlarmNotifyUser model) {

		List<AlarmNotifyUser> list = alarmNotifyUserMapper.settingUserList(model.getUserId());
		Date date = new Date();
		if (list == null || list.size() <= 0) {
			// 校验手机号码
			String telRegex = "[1][3456789]\\d{9}";
			if (model.getPhone().matches(telRegex) == true) {
				model.setPhone(model.getPhone());
			} else {
				throw new RuntimeException("请输入正确手机号码！");
			}
			// 校验邮箱格式
			String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
			if (model.getEmail().matches(str)) {
				model.setEmail(model.getEmail());
			} else {
				throw new RuntimeException("请输入正确邮箱格式！");
			}
			model.setCreateTime(date);
			alarmNotifyUserMapper.insert(model);
		} else {
			try {
				for (AlarmNotifyUser alarmNotifyUser : list) {
					// 校验手机号码
					String telRegex = "[1][3456789]\\d{9}";
					if (model.getPhone().matches(telRegex) == true) {
						model.setPhone(model.getPhone());
					} else {
						throw new RuntimeException("请输入正确手机号码！");
					}
					// 校验邮箱格式
					String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
					if (model.getEmail().matches(str)) {
						model.setEmail(model.getEmail());
					} else {
						throw new RuntimeException("请输入正确邮箱格式！");
					}
					if (model.getNickName().equals(alarmNotifyUser.getNickName())) {
						throw new RuntimeException("用户名已存在！");
					}
					if (model.getPhone().equals(alarmNotifyUser.getPhone())) {
						throw new RuntimeException("手机号码已存在！");
					}
					if (model.getEmail().equals(alarmNotifyUser.getEmail())) {
						throw new RuntimeException("邮箱已存在！");
					}
				}
				model.setCreateTime(date);
				alarmNotifyUserMapper.insert(model);
			} catch (Exception e) {
//                e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

}
