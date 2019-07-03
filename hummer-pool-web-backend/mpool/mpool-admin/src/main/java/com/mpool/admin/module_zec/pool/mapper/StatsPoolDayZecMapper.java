package com.mpool.admin.module_zec.pool.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.InModel.InBlock;
import com.mpool.admin.InModel.InBlockchain;
import com.mpool.admin.mapperUtils.pool.mapperBase.StatsPoolDayBaseMapper;
import com.mpool.common.model.pool.StatsPoolDay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StatsPoolDayZecMapper extends StatsPoolDayBaseMapper {
}
