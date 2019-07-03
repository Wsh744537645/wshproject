
package com.mpool.accountmultiple.service;

import com.mpool.common.model.account.UserWalletPayType;

import java.util.List;

public interface UserWalletPayTypeService extends BaseService<UserWalletPayType> {

	List<String> selectListByEnable();

	Integer payIdByType(String payType);
}
