package com.mpool.admin.module.account.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.InModel.InBlock;
import com.mpool.common.model.account.fpps.FppsRatioDay;

import java.util.List;

public interface FppsRatioDayService {
	/**
	 * wgg 设置费率
	 * 
	 * @param fppsRate
	 */
	void updateFppsRateDay(Float fppsRate);

//	IPage<FppsRatioDay> getFppsRateDays(InBlock date, IPage<FppsRatioDay> page);

	/**
	 * wgg 获取每一天的费率明细
	 * 
	 * @param inblock
	 * @param iPage
	 * @return
	 */
	IPage<FppsRatioDay> getMinerFeeRateList(IPage<FppsRatioDay> iPage, InBlock inblock);

	/**
	 * wgg 导出excel每一天的费率明细
	 * 
	 * @param date
	 * @param page
	 * @return
	 */
	List<List<Object>> exportHistoryFppsRateDays(InBlock date, IPage<FppsRatioDay> page);
}
