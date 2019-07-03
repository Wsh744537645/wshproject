package com.mpool.admin.module.system.service;

import com.mpool.common.model.account.Currency;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/13 18:29
 */
public interface CurrencyService {
    Currency getCurrencyByName(String name);

    List<Currency> selectListByEnable();

    Integer getCurCurrencyId();
}
