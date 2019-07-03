package com.mpool.admin.cache;

import com.mpool.common.model.account.Currency;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/13 18:15
 */
public class LocalCache {
    public static List<Currency> currencies = new ArrayList<>();

    public static Currency getCurrencyByName(String name){
        for(Currency currency : currencies){
            if(currency.getType().equals(name)){
                return currency;
            }
        }
        return null;
    }
}
