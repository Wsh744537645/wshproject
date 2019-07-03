package com.mpool.admin.module_zec.pool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.admin.mapperUtils.pool.mapperBase.MiningWorkersBaseMapper;
import com.mpool.admin.module_zec.pool.model.MiningWorkersZec;
import com.mpool.common.model.pool.MiningWorkers;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MiningWorkersZecMapper extends MiningWorkersBaseMapper<MiningWorkersZec> {
}
