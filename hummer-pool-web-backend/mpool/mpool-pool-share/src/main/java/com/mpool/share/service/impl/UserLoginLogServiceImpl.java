package com.mpool.share.service.impl;

import com.mpool.share.mapper.UserLoginLogMapper;
import com.mpool.share.service.UserLoginLogService;
import com.mpool.common.model.account.log.UserLoginLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class UserLoginLogServiceImpl implements UserLoginLogService {
	@Autowired
	private UserLoginLogMapper userLoginLogMapper;

	@Override
	public void insert(UserLoginLog entity) {
		userLoginLogMapper.insert(entity);
	}

	@Override
	public void inserts(List<UserLoginLog> entitys) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(UserLoginLog entity) {
		userLoginLogMapper.updateById(entity);
	}

	@Override
	public void delete(Serializable id) {
		userLoginLogMapper.deleteById(id);
	}

	@Override
	public UserLoginLog findById(Serializable id) {
		return userLoginLogMapper.selectById(id);
	}

}
