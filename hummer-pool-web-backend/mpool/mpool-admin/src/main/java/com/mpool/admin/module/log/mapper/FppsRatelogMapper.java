package com.mpool.admin.module.log.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mpool.common.model.account.log.FppsRateLog;

@Mapper
public interface FppsRatelogMapper extends BaseMapper<FppsRateLog> {

	IPage<Map<String, Object>> getFppsRateChangeLog(IPage<FppsRateLog> page,
			@Param("fppsRateLog") FppsRateLog fppsRateLog);

}
