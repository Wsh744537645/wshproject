package com.mpool.accountmultiple.service.bill;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.account.bill.UserPayBillItem;
import com.mpool.common.model.account.fpps.UserFppsRecord;

import java.util.List;
import java.util.Map;

public interface UserPayBillItemService {

	IPage<Map<String, Object>> getPayRecord(IPage<Map<String, Object>> page, UserPayBillItem wallet);

	/**
	 * 获得用户支付中的 btc
	 * 
	 * @param puid
	 * @return
	 */
	String getUserPayBtc(Long puid);

	List<UserPayBillItem> getUserPayInPayment(List<Long> userIds);

	/**
	 * 获得用户昨日支付
	 * 
	 * @param userId
	 * @return
	 */
	Long getUserPayYesterday(Long userId);

	/**
	 * 获得用户昨日已支付
	 * 
	 * @param userIds
	 * @return
	 */
	Map<Long, Long> getUsersPayYesterday(List<Long> userIds);

	/**
	 * 获得用户收益记录
	 * 
	 * @param page
	 * @param userId
	 * @return
	 */
	IPage<UserFppsRecord> getIncomeRecord(IPage<UserFppsRecord> page, Long userId);

	List<List<Object>> getPayRecordExport(UserPayBillItem wallet);
}
