package com.mpool.share.service;

/**
 * @Author: stephen
 * @Date: 2019/5/22 17:02
 */
public interface RealTimeDataService {

    /**
     * 通过支付方式获得当前1单位数字货币对应的美元数量
     * @param payId
     * @return
     */
    Double getCurCurrencyPriceToUsd(Integer payId);

    /**
     * 获得一美元对应的货币数量
     * @param payId
     * @return
     */
    Double getCurUsdToCurrency(Integer payId);
}
