package com.mpool.accountmultiple.service.bill;

import com.mpool.mpoolaccountmultiplecommon.model.WalletModel;
import com.mpool.accountmultiple.service.BaseService;
import com.mpool.common.model.account.bill.UserPay;

import java.math.BigDecimal;
import java.util.List;

public interface UserPayService extends BaseService<UserPay> {
	WalletModel findWallets(Long userId);

	WalletModel getWalletInfo(Long userId);

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

}
