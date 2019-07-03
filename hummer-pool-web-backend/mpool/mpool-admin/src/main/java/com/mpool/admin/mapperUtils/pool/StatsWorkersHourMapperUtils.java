package com.mpool.admin.mapperUtils.pool;

import com.mpool.admin.exception.AdminException;
import com.mpool.admin.exception.ExceptionCode;
import com.mpool.admin.mapperUtils.pool.mapperBase.StatsWorkersHourBaseMapper;
import com.mpool.admin.module.pool.mapper.StatsWorkersHourMapper;
import com.mpool.admin.module_zec.pool.mapper.StatsWorkersHourZecMapper;
import com.mpool.admin.utils.AdminSecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/5/14 16:19
 */

@Component
@Slf4j
public class StatsWorkersHourMapperUtils implements StatsWorkersHourBaseMapper {
    @Autowired
    private StatsWorkersHourMapper statsWorkersHourMapper;
    @Autowired
    private StatsWorkersHourZecMapper statsWorkersHourZecMapper;

    protected StatsWorkersHourBaseMapper getInstance(){
        String currency = AdminSecurityUtils.getCurrencyName();
        if(currency == null || currency.equals("BTC")){
            return statsWorkersHourMapper;
        }else if(currency.equals("ZEC")){
            return statsWorkersHourZecMapper;
        }
        log.error("StatsWorkersHourMapperUtils 调用失败， currency={}", AdminSecurityUtils.getCurrencyName());
        throw new AdminException(ExceptionCode.service_not_exist);
    }


    @Override
    public Integer getActiveWorker(String hour) {
        return getInstance().getActiveWorker(hour);
    }

    @Override
    public List<Map<String, Object>> getStatsUsersHourInHour(Long userId, List<String> hours) {
        return getInstance().getStatsUsersHourInHour(userId, hours);
    }
}
