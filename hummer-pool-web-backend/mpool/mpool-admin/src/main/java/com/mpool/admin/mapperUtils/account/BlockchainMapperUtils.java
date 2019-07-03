package com.mpool.admin.mapperUtils.account;

import com.mpool.admin.exception.AdminException;
import com.mpool.admin.exception.ExceptionCode;
import com.mpool.admin.mapperUtils.account.mapperBase.BlockchainBaseMapper;
import com.mpool.admin.module.account.mapper.BlockchainMapper;
import com.mpool.admin.module_zec.account.mapper.BlockchainZecMapper;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.common.model.account.Blockchain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/13 17:09
 */

@Component
@Slf4j
public class BlockchainMapperUtils implements BlockchainBaseMapper {
    @Autowired
    private BlockchainMapper blockchainMapper;
    private BlockchainZecMapper blockchainZecMapper;

    protected BlockchainBaseMapper getInstance(){
        String currency = AdminSecurityUtils.getCurrencyName();
        if(currency == null || currency.equals("BTC")){
            return blockchainMapper;
        }else if(currency.equals("ZEC")){
            return blockchainZecMapper;
        }
        log.error("BlockchainMapperUtils 调用失败， currency={}", AdminSecurityUtils.getCurrencyName());
        throw new AdminException(ExceptionCode.service_not_exist);
    }

    @Override
    public List<Blockchain> gethashRate(List<String> days) {
        return getInstance().gethashRate(days);
    }
}
