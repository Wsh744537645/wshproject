package com.mpool.admin.mapperUtils.recommend;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.exception.AdminException;
import com.mpool.admin.exception.ExceptionCode;
import com.mpool.admin.mapperUtils.recommend.mapperBase.RecommendApartCoinBaseMapper;
import com.mpool.admin.module.recommend.mapper.RecommendApartCoinMapper;
import com.mpool.admin.module_zec.recommend.mapper.RecommendApartCoinZecMapper;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.common.model.account.UserFeeRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/14 16:26
 */
@Component
@Slf4j
public class RecommendApartCoinMapperUtils implements RecommendApartCoinBaseMapper {
    @Autowired
    private RecommendApartCoinMapper recommendApartCoinMapper;
    @Autowired
    private RecommendApartCoinZecMapper recommendApartCoinZecMapper;

    protected RecommendApartCoinBaseMapper getInstance(){
        String currency = AdminSecurityUtils.getCurrencyName();
        if(currency == null || currency.equals("BTC")){
            return recommendApartCoinMapper;
        }else if(currency.equals("ZEC")){
            return recommendApartCoinZecMapper;
        }
        log.error("RecommendApartCoinMapperUtils 调用失败， currency={}", AdminSecurityUtils.getCurrencyName());
        throw new AdminException(ExceptionCode.service_not_exist);
    }

    @Override
    public IPage<UserFeeRecord> getHistoryApartCoin(IPage<UserFeeRecord> page, Date str, Date end) {
        return getInstance().getHistoryApartCoin(page, str, end);
    }

    @Override
    public List<UserFeeRecord> getHistoryRecommendApartCoin(List<Integer> days) {
        return getInstance().getHistoryRecommendApartCoin(days);
    }

    @Override
    public Long sumApartCoin(Integer type, Date str, Date end) {
        return getInstance().sumApartCoin(type, str, end);
    }

    @Override
    public Long getCurrentCoin(Date day) {
        return getInstance().getCurrentCoin(day);
    }
}
