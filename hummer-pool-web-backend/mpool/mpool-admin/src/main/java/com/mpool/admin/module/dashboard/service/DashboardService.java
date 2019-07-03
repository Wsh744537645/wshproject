package com.mpool.admin.module.dashboard.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.InModel.InBlock;
import com.mpool.admin.InModel.InBlockchain;
import com.mpool.common.model.account.BlockchainAllModel;
import com.mpool.common.model.account.FoundBlocks;
import com.mpool.common.model.pool.StatsPoolDay;
import com.mpool.common.model.pool.StatsPoolHour;

import java.util.List;
import java.util.Map;

public interface DashboardService {

	Map<String, Object> currentPoolInfo();

	List<StatsPoolDay> getPoolPast30DayShare();
    List<StatsPoolDay> getPoolPast30DayShareCache();

	List<Map<String, Object>> getPoolPast30DayWorker();
	List<Map<String, Object>> getPoolPast30DayWorkerCache();

	/**
	 * 获取全部爆块记录
	 * @return
	 */
	List<FoundBlocks> getHistoryBlock();

	List<StatsPoolHour> getPoolPast24HShare();

	/**
	 * wgg
	 * 根据时间段搜索爆块记录带分页
	 * @param date
	 * @param page
	 * @return
	 */
	IPage<FoundBlocks> getHistoryBlockList(InBlock date , IPage<FoundBlocks> page);
//	IPage<FoundBlocks> getHistoryBlockPageList(IPage<FoundBlocks> page);

	/**
	 * wgg
	 * 导出爆块记录（按时间段搜索）
	 * @param date
	 * @return
	 */
	List<List<Object>> exportHistoryBlockList(InBlock date);

	/**
	 * 全网算力和全网爆块数
	 * @return
	 */
	IPage<InBlockchain> getBlockchainInfo(IPage<InBlockchain> page,InBlock date );
}
