package com.mpool.admin.module.log.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mpool.admin.module.log.mapper.FppsRatelogMapper;
import com.mpool.admin.module.log.service.FppsRatelogService;
import com.mpool.common.model.account.log.FppsRateLog;

@Service
public class FppsRatelogServiceImpl implements FppsRatelogService {
	@Autowired
	private FppsRatelogMapper fppsRatelogMapper;

	public IPage<Map<String, Object>> getFppsRateChangeLog(IPage<FppsRateLog> page, FppsRateLog fppsRateLog) {
		return fppsRatelogMapper.getFppsRateChangeLog(page, fppsRateLog);
	}

}
