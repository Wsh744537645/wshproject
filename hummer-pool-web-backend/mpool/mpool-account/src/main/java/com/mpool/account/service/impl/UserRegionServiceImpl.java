package com.mpool.account.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpool.account.mapper.UserRegionMapper;
import com.mpool.account.service.UserRegionSerice;
import com.mpool.common.model.account.UserRegion;

@Service
public class UserRegionServiceImpl implements UserRegionSerice {
	@Autowired
	private UserRegionMapper userRegionMapper;

	@Override
	public void seva(UserRegion userRegion) {
		userRegionMapper.insert(userRegion);
	}

	@Override
	public String findRegionNameByUserId(Long userId) {
		return userRegionMapper.findRegionNameByUserId(userId);
	}

	@Override
	public Integer findRegionIdByUserId(Long userId) {
		// TODO Auto-generated method stub
		return userRegionMapper.findRegionIdByUserId(userId);
	}

}
