package com.mpool.share.admin.module.bill.service.impl;

import com.mpool.share.admin.exception.ExceptionCode;
import com.mpool.share.admin.exception.AdminException;
import com.mpool.share.admin.module.bill.service.RealTimeDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: stephen
 * @Date: 2019/5/22 17:06
 */

@Service
@Slf4j
public class RealTimeDataServiceImpl implements RealTimeDataService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Double getCurCurrencyPriceToUsd(Integer payId) {
        Double usdPreBtc = new Double(0);
        int count = 5;
        if (payId.equals(1)) {
            while (true) {
                try {
                    String btcPreUsd = restTemplate.getForObject("https://blockchain.info/tobtc?currency=USD&value=1", String.class);
                    usdPreBtc = 1.0D / (Double.parseDouble(btcPreUsd));
                    break;
                }
                catch (Exception e){
                    log.error("getCurCurrencyPriceToUsd error");
                    if(count <= 0){
                        break;
                    }
                }
                count --;
            }
        } else {
            throw new AdminException(ExceptionCode.param_ex);
        }
        return usdPreBtc;
    }

    @Override
    public Double getCurUsdToCurrency(Integer payId) {
        Double usdPreBtc = new Double(0);
        int count = 5;
        if (payId.equals(1)) {
            while (true) {
                try {
                    String btcPreUsd = restTemplate.getForObject("https://blockchain.info/tobtc?currency=USD&value=1", String.class);
                    usdPreBtc = Double.parseDouble(btcPreUsd);
                    break;
                } catch (Exception e) {
                    log.error("getCurUsdToCurrency error");
                    if(count <= 0){
                        break;
                    }
                }
                count--;
            }
        } else {
            throw new AdminException(ExceptionCode.param_ex);
        }
        return usdPreBtc;
    }
}
