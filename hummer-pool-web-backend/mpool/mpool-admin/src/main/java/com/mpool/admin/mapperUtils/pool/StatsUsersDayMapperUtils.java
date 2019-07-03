package com.mpool.admin.mapperUtils.pool;

import com.mpool.admin.exception.AdminException;
import com.mpool.admin.exception.ExceptionCode;
import com.mpool.admin.mapperUtils.pool.mapperBase.StatsUsersDayBaseMapper;
import com.mpool.admin.module.pool.mapper.StatsUsersDayMapper;
import com.mpool.admin.module_zec.pool.mapper.StatsUsersDayZecMapper;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.common.model.pool.StatsUsersDay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/14 16:01
 */

@Component
@Slf4j
public class StatsUsersDayMapperUtils implements StatsUsersDayBaseMapper {
    @Autowired
    private StatsUsersDayMapper statsUsersDayMapper;
    @Autowired
    private StatsUsersDayZecMapper statsUsersDayZecMapper;

    protected StatsUsersDayBaseMapper getInstance(){
        String currency = AdminSecurityUtils.getCurrencyName();
        if(currency == null || currency.equals("BTC")){
            return statsUsersDayMapper;
        }else if(currency.equals("ZEC")){
            return statsUsersDayZecMapper;
        }
        log.error("StatsUsersDayMapperUtils 调用失败， currency={}", AdminSecurityUtils.getCurrencyName());
        throw new AdminException(ExceptionCode.service_not_exist);
    }

    @Override
    public List<Long> getShareByPuidAndDay(List<Long> puid, Long day) {
        return getInstance().getShareByPuidAndDay(puid, day);
    }

    @Override
    public List<StatsUsersDay> getStatsUsersDayInDay(Long userId, List<String> get30Day) {
        return getInstance().getStatsUsersDayInDay(userId, get30Day);
    }

    @Override
    public List<StatsUsersDay> getStatsUsersDayInDayAndInPuid(List<Long> subUserIds, List<String> days) {
        return getInstance().getStatsUsersDayInDayAndInPuid(subUserIds, days);
    }
}
