package com.mpool.accountmultiple.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.account.User;

import java.util.List;

public interface UserService extends BaseService<User> {

	User findByUsername(String username);

	/**
	 * 查询子账号
	 *
	 * @param userId 主账号ID
	 * @return
	 */
	List<User> findSubAccount(String userId);

	List<User> findSubAccountByCurrencyId(Long userId);

	/**
	 * userPage
	 *
	 * @param iPage
	 * @param user
	 * @return
	 */
	IPage<User> selectUserPage(IPage<User> iPage, User user);

	/**
	 * 根据钱包币种id获得用户列表
	 * @param iPage
	 * @param user
	 * @return
	 */
	IPage<User> selectUserPageByPayCurrencyId(IPage<User> iPage, Long masterId);
}
