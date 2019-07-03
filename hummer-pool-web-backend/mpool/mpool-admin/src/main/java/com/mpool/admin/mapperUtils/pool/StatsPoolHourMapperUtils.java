package com.mpool.admin.mapperUtils.pool;

import com.mpool.admin.exception.AdminException;
import com.mpool.admin.exception.ExceptionCode;
import com.mpool.admin.mapperUtils.pool.mapperBase.StatsPoolHourBaseMapper;
import com.mpool.admin.module.pool.mapper.StatsPoolHourMapper;
import com.mpool.admin.module_zec.pool.mapper.StatsPoolHourZecMapper;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.common.model.pool.StatsPoolHour;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/14 15:56
 */

@Component
@Slf4j
public class StatsPoolHourMapperUtils implements StatsPoolHourBaseMapper {
    @Autowired
    private StatsPoolHourMapper statsPoolHourMapper;
    @Autowired
    private StatsPoolHourZecMapper statsPoolHourZecMapper;

    protected StatsPoolHourBaseMapper getInstance(){
        String currency = AdminSecurityUtils.getCurrencyName();
        return getInstance(currency);
    }

    protected StatsPoolHourBaseMapper getInstance(String currencyName){
        if(currencyName == null || currencyName.equals("BTC")){
            return statsPoolHourMapper;
        }else if(currencyName.equals("ZEC")){
            return statsPoolHourZecMapper;
        }
        log.error("StatsPoolHourMapperUtils 调用失败， currency={}", AdminSecurityUtils.getCurrencyName());
        throw new AdminException(ExceptionCode.service_not_exist);
    }

    @Override
    public StatsPoolHour getStatsPoolHourByHour(String hour) {
        return getInstance().getStatsPoolHourByHour(hour);
    }

    @Override
    public List<StatsPoolHour> getStatsPoolHourByHours(List<String> hours) {
        return getInstance().getStatsPoolHourByHours(hours);
    }

    public StatsPoolHour getStatsPoolHourByHourByCurrency(String hour, String currencyName) {
        return getInstance(currencyName).getStatsPoolHourByHour(hour);
    }
}
