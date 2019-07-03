package com.mpool.admin.mapperUtils.account;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.InModel.InBlock;
import com.mpool.admin.InModel.InBlockchain;
import com.mpool.admin.exception.AdminException;
import com.mpool.admin.exception.ExceptionCode;
import com.mpool.admin.mapperUtils.account.mapperBase.BlockchainAllBaseMapper;
import com.mpool.admin.module.account.mapper.BlockchainAllMapper;
import com.mpool.admin.module_zec.account.mapper.BlockchainAllZecMapper;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.common.model.account.BlockchainAllModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: stephen
 * @Date: 2019/5/13 16:24
 */

@Component
@Slf4j
public class BlockchainAllMapperUtils implements BlockchainAllBaseMapper {
    @Autowired
    private BlockchainAllMapper blockchainAllMapper;

    @Autowired
    private BlockchainAllZecMapper blockchainAllZecMapper;

    protected BlockchainAllBaseMapper getInstance(){
        String currency = AdminSecurityUtils.getCurrencyName();
        if(currency == null || currency.equals("BTC")){
            return blockchainAllMapper;
        }else if(currency.equals("ZEC")){
            return blockchainAllZecMapper;
        }
        log.error("BlockchainAllMapperUtils 调用失败， currency={}", AdminSecurityUtils.getCurrencyName());
        throw new AdminException(ExceptionCode.service_not_exist);
    }

    @Override
    public IPage<BlockchainAllModel> getBlocks(IPage<InBlockchain> iPage, @Param("date") InBlock date){
        return getInstance().getBlocks(iPage, date);
    }

}
