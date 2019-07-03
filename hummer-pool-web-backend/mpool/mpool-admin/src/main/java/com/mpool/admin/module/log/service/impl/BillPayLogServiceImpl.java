package com.mpool.admin.module.log.service.impl;

import java.util.Map;

import com.mpool.admin.mapperUtils.log.BillPayLogMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.module.log.mapper.BillPayLogMapper;
import com.mpool.admin.module.log.service.BillPayLogService;

@Service
public class BillPayLogServiceImpl implements BillPayLogService {
	@Autowired
	private BillPayLogMapperUtils billPayLogMapper;

	@Override
	public IPage<Map<String, Object>> getBillPayLog(IPage<Map<String, Object>> ipage) {
		return billPayLogMapper.getBillPayLog(ipage);
	}

}
