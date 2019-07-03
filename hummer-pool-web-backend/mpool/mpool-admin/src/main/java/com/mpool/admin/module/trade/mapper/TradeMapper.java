package com.mpool.admin.module.trade.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;

@Mapper
public interface TradeMapper {
	IPage<Map<String, Object>> getToBePayList(IPage<Map<String, Object>> iPage);
}
