
package com.mpool.account.service;

import java.util.List;

import com.mpool.common.model.account.UserWalletPayType;

public interface UserWalletPayTypeService extends BaseService<UserWalletPayType> {

	List<String> selectListByEnable();

	Integer payIdByType(String payType);
}
