package com.mpool.admin.module.bill.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.InModel.InBlock;
import com.mpool.common.model.account.bill.UserPayBill;

import java.util.List;
import java.util.Map;

public interface UserPayBillService {
	/**
	 * 创建支付单
	 * 
	 * @param userId
	 * @param arrayList
	 * @return
	 */
	void createBillNumber(Long userId, List<Long> arrayList);

	/**
	 * 获得billinfo
	 * 
	 * @param iPage
	 * @param param 
	 * @return
	 */
	IPage<Map<String, Object>> getBillInfo(IPage<Map<String, Object>> iPage, Map<String, Object> param);

	/**
	 * 支付
	 * 
	 * @param billNumber
	 * @param txid
	 */
	void pay(String billNumber, String txid);

	/**
	 * wgg
	 * 根据单号或TXID搜索 支付信息
	 * @param billNum
	 * @return
	 */
	UserPayBill getBillInfoByNumOrTxid(String billNum,String txid);

	/**
	 * 通过时间段搜索获得billinfo
	 * @param date
	 * @param iPage
	 * @return
	 */
	Map<String, Object> getBillInfoByDate(InBlock date,IPage<Map<String, Object>> iPage);

	/**
	 * wgg
	 * 导出主账号信息（账户名，注册时间，最后登录时间，注册手机，邮箱）
	 * @param date
	 * @return
	 */
	List<List<Object>> exportBillInfo(InBlock date);

	/**
	 * 回滚待支付账单中的子账单
	 * @param billNumber
	 * @param arrayList
	 */
	void rollBackBillItem(Long userId, String billNumber, List<Long> arrayList);


}
