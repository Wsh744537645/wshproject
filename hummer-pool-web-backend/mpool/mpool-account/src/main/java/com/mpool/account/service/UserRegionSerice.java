package com.mpool.account.service;

import com.mpool.common.model.account.UserRegion;

public interface UserRegionSerice {
	void seva(UserRegion userRegion);

	String findRegionNameByUserId(Long userId);

	Integer findRegionIdByUserId(Long userId);
}
