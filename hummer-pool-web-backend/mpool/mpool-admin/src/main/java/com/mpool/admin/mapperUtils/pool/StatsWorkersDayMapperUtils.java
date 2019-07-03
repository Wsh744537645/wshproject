package com.mpool.admin.mapperUtils.pool;

import com.mpool.admin.exception.AdminException;
import com.mpool.admin.exception.ExceptionCode;
import com.mpool.admin.mapperUtils.pool.mapperBase.StatsWorkersDayBaseMapper;
import com.mpool.admin.module.pool.mapper.StatsWorkersDayMapper;
import com.mpool.admin.module_zec.pool.mapper.StatsWorkersDayZecMapper;
import com.mpool.admin.utils.AdminSecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/5/14 16:14
 */

@Component
@Slf4j
public class StatsWorkersDayMapperUtils implements StatsWorkersDayBaseMapper {
    @Autowired
    private StatsWorkersDayMapper statsWorkersDayMapper;
    @Autowired
    private StatsWorkersDayZecMapper statsWorkersDayZecMapper;

    protected StatsWorkersDayBaseMapper getInstance(){
        String currency = AdminSecurityUtils.getCurrencyName();
        if(currency == null || currency.equals("BTC")){
            return statsWorkersDayMapper;
        }else if(currency.equals("ZEC")){
            return statsWorkersDayZecMapper;
        }
        log.error("StatsWorkersDayMapperUtils 调用失败， currency={}", AdminSecurityUtils.getCurrencyName());
        throw new AdminException(ExceptionCode.service_not_exist);
    }

    @Override
    public List<Map<String, Object>> getPoolPast30DayWorker(List<String> days) {
        return getInstance().getPoolPast30DayWorker(days);
    }

    @Override
    public List<Map<String, Object>> getStatsUsersDayInDay(Long userId, List<String> get30Day) {
        return getInstance().getStatsUsersDayInDay(userId, get30Day);
    }
}
