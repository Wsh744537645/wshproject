package com.mpool.admin.module.pool.mapper;

import java.util.List;

import com.mpool.admin.mapperUtils.pool.mapperBase.MiningWorkersBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.pool.MiningWorkers;

@Mapper
public interface MiningWorkersMapper extends MiningWorkersBaseMapper<MiningWorkers> {
}
