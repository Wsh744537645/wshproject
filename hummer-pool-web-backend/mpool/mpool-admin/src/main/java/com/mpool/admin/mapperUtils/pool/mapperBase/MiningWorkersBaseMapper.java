package com.mpool.admin.mapperUtils.pool.mapperBase;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.pool.MiningWorkers;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/14 15:22
 */
public interface MiningWorkersBaseMapper<T> extends BaseMapper<T> {
    MiningWorkers getUserMiningWorkers(@Param("userId") Long userId);

    List<MiningWorkers> getUserMiningWorkersList(@Param("list") List<Long> userId);

    MiningWorkers getMiningWorkersByPool();

    Integer getPoolWorkerActiveCount();
}
