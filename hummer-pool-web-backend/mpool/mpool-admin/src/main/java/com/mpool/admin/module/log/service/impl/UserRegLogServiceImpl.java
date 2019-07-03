package com.mpool.admin.module.log.service.impl;

import java.util.Map;

import com.mpool.admin.InModel.OutUserLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.module.log.mapper.UserRegLogMapper;
import com.mpool.admin.module.log.service.UserRegLogService;
import com.mpool.common.model.account.log.UserRegLog;

@Service
public class UserRegLogServiceImpl implements UserRegLogService {

	private static final Logger log = LoggerFactory.getLogger(UserRegLogServiceImpl.class);

	@Autowired
	private UserRegLogMapper userRegLogMapper;

	@Override
	public IPage<Map<String, Object>> getAccountRegisterLog(IPage<UserRegLog> page, UserRegLog userRegLog) {

		return userRegLogMapper.getAccountRegisterLog(page, userRegLog);
	}

	@Override
	public IPage<OutUserLog> getUserLog(IPage<OutUserLog> page, String username) {

		return userRegLogMapper.getUserLog(page,username);
	}

}
