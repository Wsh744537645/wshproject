package com.mpool.admin.mapperUtils.pool;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.InModel.InBlock;
import com.mpool.admin.InModel.InBlockchain;
import com.mpool.admin.exception.AdminException;
import com.mpool.admin.exception.ExceptionCode;
import com.mpool.admin.mapperUtils.pool.mapperBase.StatsPoolDayBaseMapper;
import com.mpool.admin.module.pool.mapper.StatsPoolDayMapper;
import com.mpool.admin.module_zec.pool.mapper.StatsPoolDayZecMapper;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.common.model.pool.StatsPoolDay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/14 15:50
 */

@Component
@Slf4j
public class StatsPoolDayMapperUtils implements StatsPoolDayBaseMapper {
    @Autowired
    private StatsPoolDayMapper statsPoolDayMapper;
    @Autowired
    private StatsPoolDayZecMapper statsPoolDayZecMapper;

    protected StatsPoolDayBaseMapper getInstance(){
        String currency = AdminSecurityUtils.getCurrencyName();
        if(currency == null || currency.equals("BTC")){
            return statsPoolDayMapper;
        }else if(currency.equals("ZEC")){
            return statsPoolDayZecMapper;
        }
        log.error("StatsPoolDayMapperUtils 调用失败， currency={}", AdminSecurityUtils.getCurrencyName());
        throw new AdminException(ExceptionCode.service_not_exist);
    }

    @Override
    public List<StatsPoolDay> getPoolPast30DayShare(List<String> days) {
        return getInstance().getPoolPast30DayShare(days);
    }

    @Override
    public IPage<InBlockchain> getPoolPast20DayShare(IPage<InBlockchain> page, List<String> days) {
        return getInstance().getPoolPast20DayShare(page, days);
    }

    @Override
    public InBlockchain getStatsPoolDayByDay(String day) {
        return getInstance().getStatsPoolDayByDay(day);
    }

    @Override
    public IPage<InBlockchain> getPoolDayShare(IPage<InBlockchain> page, InBlock date) {
        return getInstance().getPoolDayShare(page, date);
    }
}
