package com.mpool.admin.module.log.service;

import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.account.log.FppsRateLog;

public interface FppsRatelogService {

	/**
	 * 获得FPPS 修改日志
	 * @param page
	 * @param fppsRateLog
	 * @return
	 */
	IPage<Map<String, Object>> getFppsRateChangeLog(IPage<FppsRateLog> page, FppsRateLog fppsRateLog);

}
