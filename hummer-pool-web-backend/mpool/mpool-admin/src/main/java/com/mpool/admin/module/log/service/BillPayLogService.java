package com.mpool.admin.module.log.service;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;

public interface BillPayLogService {
	IPage<Map<String, Object>> getBillPayLog(IPage<Map<String, Object>> ipage);
}
