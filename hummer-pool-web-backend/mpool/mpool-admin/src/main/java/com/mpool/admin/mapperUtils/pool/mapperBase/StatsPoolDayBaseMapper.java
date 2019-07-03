package com.mpool.admin.mapperUtils.pool.mapperBase;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.InModel.InBlock;
import com.mpool.admin.InModel.InBlockchain;
import com.mpool.common.model.pool.StatsPoolDay;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/14 15:50
 */
public interface StatsPoolDayBaseMapper {
    List<StatsPoolDay> getPoolPast30DayShare(@Param("days") List<String> days);

    IPage<InBlockchain> getPoolPast20DayShare(IPage<InBlockchain> page, @Param("days") List<String> days);

    InBlockchain getStatsPoolDayByDay(@Param("day") String day);

    IPage<InBlockchain> getPoolDayShare(IPage<InBlockchain> page, @Param("date") InBlock date);
}
