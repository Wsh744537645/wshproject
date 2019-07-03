package com.mpool.account.service.bill;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.mpool.account.model.UserPayModel;
import com.mpool.account.model.WalletModel;
import com.mpool.account.service.BaseService;
import com.mpool.common.model.account.bill.UserPay;

public interface UserPayService extends BaseService<UserPay> {
	WalletModel findWallets(Long userId);

	WalletModel getWalletInfo(Long userId, Integer currencyId);

	/**
	 * 增加钱包
	 * @param userId
	 * @param userPayModel
	 * @return
	 */
	int addWalletInfo(Long userId, UserPayModel userPayModel);

	/**
	 * 获得历史总支付
	 * 
	 * @param userId
	 * @return
	 */
	UserPay getPayHistory(Long userId);

	/**
	 * 获得预支付
	 * 
	 * @param userId
	 * @return
	 */
	BigDecimal getPayCurrent(Long userId);

	/**
	 * 每天定时 算账
	 */
	void taskBill();

	List<UserPay> getUserPayByPuid(List<Long> userIds);

	/**
	 * 获得指定用户id列表且非指定币种的钱包信息
	 * @param ids
	 * @param currencyId
	 * @return
	 */
	List<Map<String, Object>> getUserPayByIdWithoutCid(List<Long> ids, Integer currencyId);
}
