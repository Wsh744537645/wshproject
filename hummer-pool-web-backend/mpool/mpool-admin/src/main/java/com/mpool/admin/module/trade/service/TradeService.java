package com.mpool.admin.module.trade.service;

import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 交易
 * @author chen
 *
 */
public interface TradeService {

	IPage<Map<String, Object>> getToBePayList(IPage<Map<String, Object>> iPage);
	
}
