package com.mpool.admin.mapperUtils.account;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.exception.AdminException;
import com.mpool.admin.exception.ExceptionCode;
import com.mpool.admin.mapperUtils.account.mapperBase.FoundBlocksBaseMapper;
import com.mpool.admin.module.account.mapper.FoundBlocksMapper;
import com.mpool.admin.module_zec.account.mapper.FoundBlocksZecMapper;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.common.model.account.FoundBlocks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/13 17:16
 */

@Component
@Slf4j
public class FoundBlocksMapperUtils implements FoundBlocksBaseMapper {
    @Autowired
    private FoundBlocksMapper foundBlocksMapper;
    @Autowired
    private FoundBlocksZecMapper foundBlocksZecMapper;

    protected FoundBlocksBaseMapper getInstance(){
        String currency = AdminSecurityUtils.getCurrencyName();
        return getInstance(currency);
    }

    protected FoundBlocksBaseMapper getInstance(String currencyName){
        if(currencyName == null || currencyName.equals("BTC")){
            return foundBlocksMapper;
        }else if(currencyName.equals("ZEC")){
            return foundBlocksZecMapper;
        }
        log.error("FoundBlocksMapperUtils 调用失败， currency={}", AdminSecurityUtils.getCurrencyName());
        throw new AdminException(ExceptionCode.service_not_exist);
    }

    @Override
    public Integer getBlock() {
        return getInstance().getBlock();
    }

    public Integer getBlockBycurrency(String currencyName) {
        return getInstance(currencyName).getBlock();
    }

    @Override
    public Long getRevenue() {
        return getInstance().getRevenue();
    }

    @Override
    public Integer getNowBlock(Date now) {
        return getInstance().getNowBlock(now);
    }

    @Override
    public Long getNowRevenue(Date now) {
        return getInstance().getNowRevenue(now);
    }

    @Override
    public List<FoundBlocks> getHistoryBlock() {
        return getInstance().getHistoryBlock();
    }

    @Override
    public IPage<FoundBlocks> getHistoryBlockList(IPage<FoundBlocks> page, Date strTime, Date endTime) {
        return getInstance().getHistoryBlockList(page, strTime, endTime);
    }

    @Override
    public IPage<FoundBlocks> getHistoryBlocks(IPage<FoundBlocks> page) {
        return getInstance().getHistoryBlocks(page);
    }

    @Override
    public List<FoundBlocks> exportHistoryBlockList(Date strTime, Date endTime) {
        return getInstance().exportHistoryBlockList(strTime, endTime);
    }
}
