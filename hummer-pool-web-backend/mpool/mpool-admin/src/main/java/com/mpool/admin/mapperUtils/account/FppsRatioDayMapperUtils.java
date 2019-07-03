package com.mpool.admin.mapperUtils.account;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.exception.AdminException;
import com.mpool.admin.exception.ExceptionCode;
import com.mpool.admin.mapperUtils.account.mapperBase.FppsRatioDayBaseMapper;
import com.mpool.admin.module.account.mapper.FppsRatioDayMapper;
import com.mpool.admin.module_zec.account.mapper.FppsRatioDayZecMapper;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.common.model.account.fpps.FppsRatioDay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author: stephen
 * @Date: 2019/5/13 17:32
 */

@Component
@Slf4j
public class FppsRatioDayMapperUtils implements FppsRatioDayBaseMapper {
    @Autowired
    private FppsRatioDayMapper fppsRatioDayMapper;
    @Autowired
    private FppsRatioDayZecMapper fppsRatioDayZecMapper;

    protected FppsRatioDayBaseMapper getInstance(){
        String currency = AdminSecurityUtils.getCurrencyName();
        if(currency == null || currency.equals("BTC")){
            return fppsRatioDayMapper;
        }else if(currency.equals("ZEC")){
            return fppsRatioDayZecMapper;
        }
        log.error("FoundBlocksMapperUtils 调用失败， currency={}", AdminSecurityUtils.getCurrencyName());
        throw new AdminException(ExceptionCode.service_not_exist);
    }


    @Override
    public IPage<FppsRatioDay> getFppsRatioDay(IPage<FppsRatioDay> iPage, Date strTime, Date endTime) {
        return getInstance().getFppsRatioDay(iPage, strTime, endTime);
    }

    @Override
    public void updateFppsRateDay(Integer day, Float fppsRate) {
        getInstance().updateFppsRateDay(day, fppsRate);
    }

    @Override
    public IPage<FppsRatioDay> getFppsRateDays(IPage<FppsRatioDay> page, Integer str, Integer end) {
        return getInstance().getFppsRateDays(page, str, end);
    }
}
