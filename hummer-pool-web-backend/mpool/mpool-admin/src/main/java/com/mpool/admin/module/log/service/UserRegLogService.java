package com.mpool.admin.module.log.service;

import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.InModel.OutUserLog;
import com.mpool.common.model.account.log.UserRegLog;

public interface UserRegLogService {

	/**
	 * 分页查询用户注册日志列表
	 * 
	 * @param page
	 * @param userRegLog
	 * @return
	 */
	IPage<Map<String, Object>> getAccountRegisterLog(IPage<UserRegLog> page, UserRegLog userRegLog);

	/**
	 * 获取用户登录后的操作日志
	 */
	IPage<OutUserLog> getUserLog(IPage<OutUserLog> page,String name);
}
