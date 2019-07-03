package com.mpool.share.service.impl;

import com.mpool.share.mapper.UserRegionMapper;
import com.mpool.share.service.UserRegionSerice;
import com.mpool.common.model.account.UserRegion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
