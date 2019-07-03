package com.mpool.admin.module.bill.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.account.bill.UserPayBillItem;
import org.apache.ibatis.annotations.Param;

public interface UserPayBillItemService {
	/**
	 * 获得未支付的 项
	 * 
	 * @param iPage
	 * @param
	 * @return
	 */
	IPage<Map<String, Object>> getDuePayItems(IPage<Map<String, Object>> iPage, Map<String, Object> param, Long day);
	//余额合计
	List<UserPayBillItem> getDuePayBtcSum(@Param("username") String username);
	/**
	 * 导出csv
	 * 
	 * @param billNumber
	 * @return
	 */
	List<List<Object>> getCSVDataItems(String billNumber);

	List<LinkedHashMap<String, Object>> getBillItemInfo(String billNumber);

	/**
	 * 获得已支付的账单
	 * 
	 * @param page
	 * @param param
	 * @return
	 */
	IPage<Map<String, Object>> getPayBillItemList(IPage<Map<String, Object>> page, Map<String, Object> param);

	/**
	 * wgg 获取待生成账单列表 通过用户名查询
	 * 
	 * @param iPage
	 * @param username
	 * @return
	 */
	IPage<UserPayBillItem> getDuePayItemList(IPage<UserPayBillItem> iPage, String username);

	/**
	 * 导出待生成账单
	 * 
	 * @param username
	 * @return
	 */
	List<List<Object>> exportDuePayItems(String username);

	/**
	 * wgg 待生成账单页面的新增账单功能 新增补发 奖励等
	 * 
	 * @param userPayBillItem
	 */
	void addPayBillInfo(UserPayBillItem userPayBillItem);

	/**
	 * wgg
	 * 批量修改待生成订单余额
	 * @param payBtc
	 */
	void updateListPayBct(Double payBtc);
}
