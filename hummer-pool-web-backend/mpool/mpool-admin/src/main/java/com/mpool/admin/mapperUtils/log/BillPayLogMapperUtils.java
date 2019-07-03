package com.mpool.admin.mapperUtils.log;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.exception.AdminException;
import com.mpool.admin.exception.ExceptionCode;
import com.mpool.admin.mapperUtils.log.mapperBase.BillPayLogBaseMapper;
import com.mpool.admin.module.log.mapper.BillPayLogMapper;
import com.mpool.admin.module_zec.log.mapper.BillPayLogZecMapper;
import com.mpool.admin.utils.AdminSecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/5/14 14:28
 */

@Component
@Slf4j
public class BillPayLogMapperUtils implements BillPayLogBaseMapper {
    @Autowired
    private BillPayLogMapper billPayLogMapper;
    @Autowired
    private BillPayLogZecMapper billPayLogZecMapper;

    protected BillPayLogBaseMapper getInstance(){
        String currency = AdminSecurityUtils.getCurrencyName();
        if(currency == null || currency.equals("BTC")){
            return billPayLogMapper;
        }else if(currency.equals("ZEC")){
            return billPayLogZecMapper;
        }
        log.error("BillPayLogMapperUtils 调用失败， currency={}", AdminSecurityUtils.getCurrencyName());
        throw new AdminException(ExceptionCode.service_not_exist);
    }

    @Override
    public IPage<Map<String, Object>> getBillPayLog(IPage<Map<String, Object>> ipage) {
        return getInstance().getBillPayLog(ipage);
    }
}
