package com.mpool.admin.module.trade.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.module.trade.mapper.TradeMapper;
import com.mpool.admin.module.trade.service.TradeService;

@Service
public class TradeServiceImpl implements TradeService {
	@Autowired
	private TradeMapper tradeMapper;

	@Override
	public IPage<Map<String, Object>> getToBePayList(IPage<Map<String, Object>> iPage) {
		return tradeMapper.getToBePayList(iPage);
	}

}
