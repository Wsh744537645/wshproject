package com.mpool.admin.mapperUtils.pool;

import com.mpool.admin.exception.AdminException;
import com.mpool.admin.exception.ExceptionCode;
import com.mpool.admin.mapperUtils.pool.mapperBase.StatsUsersHourBaseMapper;
import com.mpool.admin.module.pool.mapper.StatsUsersHourMapper;
import com.mpool.admin.module_zec.pool.mapper.StatsUsersHourZecMapper;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.common.model.pool.StatsUsersHour;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/14 16:09
 */

@Component
@Slf4j
public class StatsUsersHourMapperUtils implements StatsUsersHourBaseMapper {
    @Autowired
    private StatsUsersHourMapper statsUsersHourMapper;
    @Autowired
    private StatsUsersHourZecMapper statsUsersHourZecMapper;

    protected StatsUsersHourBaseMapper getInstance(){
        String currency = AdminSecurityUtils.getCurrencyName();
        if(currency == null || currency.equals("BTC")){
            return statsUsersHourMapper;
        }else if(currency.equals("ZEC")){
            return statsUsersHourZecMapper;
        }
        log.error("StatsUsersHourMapperUtils 调用失败， currency={}", AdminSecurityUtils.getCurrencyName());
        throw new AdminException(ExceptionCode.service_not_exist);
    }


    @Override
    public List<StatsUsersHour> getUsers24HourShare(List<Long> arrayList, List<String> past24Hour) {
        return getInstance().getUsers24HourShare(arrayList, past24Hour);
    }
}
