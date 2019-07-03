package com.mpool.admin.module.system.service.impl;

import com.mpool.admin.cache.LocalCache;
import com.mpool.admin.module.system.mapper.CurrencyMapper;
import com.mpool.admin.module.system.service.CurrencyService;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.common.model.account.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/13 18:31
 */

@Service
public class CurrencyServiceImpl implements CurrencyService {
    @Autowired
    private CurrencyMapper currencyMapper;

    @Override
    public Currency getCurrencyByName(String name) {
        if(LocalCache.currencies.isEmpty()){
            LocalCache.currencies = currencyMapper.getAllCurrency();
        }

        return LocalCache.getCurrencyByName(name);
    }

    @Override
    public List<Currency> selectListByEnable() {
        if(LocalCache.currencies.isEmpty()){
            LocalCache.currencies = currencyMapper.getAllCurrency();
        }

        return LocalCache.currencies;
    }

    @Override
    public Integer getCurCurrencyId() {
        if(LocalCache.currencies.isEmpty()){
            LocalCache.currencies = currencyMapper.getAllCurrency();
        }

        return LocalCache.getCurrencyByName(AdminSecurityUtils.getCurrencyName()).getId();
    }
}
