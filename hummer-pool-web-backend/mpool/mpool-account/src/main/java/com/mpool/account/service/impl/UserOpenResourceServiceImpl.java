package com.mpool.account.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mpool.account.mapper.UserOpenResourceMapper;
import com.mpool.account.service.UserOpenResourceService;
import com.mpool.common.model.account.UserOpenResource;
import com.mpool.common.util.UUIDUtil;

@Service
public class UserOpenResourceServiceImpl implements UserOpenResourceService {
	@Autowired
	private UserOpenResourceMapper userOpenResourceMapper;

	@Override
	public void insert(UserOpenResource entity) {
		String resId = UUIDUtil.generateUUID();
		entity.setResId(resId);
		userOpenResourceMapper.insert(entity);
	}

	@Override
	public void inserts(List<UserOpenResource> entitys) {
		for (UserOpenResource entity : entitys) {
			userOpenResourceMapper.insert(entity);
		}
	}

	@Override
	public void update(UserOpenResource entity) {
		userOpenResourceMapper.updateById(entity);
	}

	@Override
	public void delete(Serializable id) {
		userOpenResourceMapper.deleteById(id);
	}

	@Override
	public UserOpenResource findById(Serializable id) {
		return userOpenResourceMapper.selectById(id);
	}

	@Override
	public String insertRet(UserOpenResource entity) {
		if (entity.getResId() == null) {
			entity.setResId(UUIDUtil.generateUUID() + "-" + entity.getCurrencyName());
		}
		userOpenResourceMapper.insert(entity);
		return entity.getResId();
	}

}
