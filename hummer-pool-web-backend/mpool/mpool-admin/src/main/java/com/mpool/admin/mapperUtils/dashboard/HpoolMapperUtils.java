package com.mpool.admin.mapperUtils.dashboard;

import com.mpool.admin.exception.AdminException;
import com.mpool.admin.exception.ExceptionCode;
import com.mpool.admin.mapperUtils.bill.mapperBase.UserPayBillBaseMapper;
import com.mpool.admin.mapperUtils.dashboard.mapperBase.HpoolBaseMapper;
import com.mpool.admin.module.dashboard.mapper.HpoolMapper;
import com.mpool.admin.module_zec.dashboard.mapper.HpoolZecMapper;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.common.model.pool.StatsUsersHour;
import com.mpool.common.model.pool.StatsWorkersHour;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/14 12:26
 */

@Component
@Slf4j
public class HpoolMapperUtils implements HpoolBaseMapper {
    @Autowired
    private HpoolMapper hpoolMapper;
    @Autowired
    private HpoolZecMapper hpoolZecMapper;

    protected HpoolBaseMapper getInstance(){
        String currency = AdminSecurityUtils.getCurrencyName();
        if(currency == null || currency.equals("BTC")){
            return hpoolMapper;
        }else if(currency.equals("ZEC")){
            return hpoolZecMapper;
        }
        log.error("HpoolMapperUtils 调用失败， currency={}", AdminSecurityUtils.getCurrencyName());
        throw new AdminException(ExceptionCode.service_not_exist);
    }

    @Override
    public List<StatsUsersHour> getStatsUsersHourInHour(Long userId, List<String> hours) {
        return getInstance().getStatsUsersHourInHour(userId, hours);
    }

    @Override
    public List<StatsWorkersHour> getStatsWorkerHourByWorkerId(Long workerId, List<String> hours) {
        return getInstance().getStatsWorkerHourByWorkerId(workerId, hours);
    }
}
