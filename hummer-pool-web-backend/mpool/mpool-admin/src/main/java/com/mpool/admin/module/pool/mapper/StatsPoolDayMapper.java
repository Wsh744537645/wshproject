package com.mpool.admin.module.pool.mapper;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.InModel.InBlock;
import com.mpool.admin.InModel.InBlockchain;
import com.mpool.admin.mapperUtils.pool.mapperBase.StatsPoolDayBaseMapper;
import com.mpool.common.model.account.Blockchain;
import com.mpool.common.model.account.BlockchainAllModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mpool.common.model.pool.StatsPoolDay;

@Mapper
public interface StatsPoolDayMapper extends StatsPoolDayBaseMapper {
}
