package com.mpool.account.service;

import com.mpool.common.model.account.UserOpenResource;

public interface UserOpenResourceService extends BaseService<UserOpenResource> {
	String insertRet(UserOpenResource entity);
}
