package com.mpool.share.service.impl;

import com.mpool.share.exception.ExceptionCode;
import com.mpool.share.exception.ShareException;
import com.mpool.share.service.RealTimeDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

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
            throw new ShareException(ExceptionCode.pay_type_error);
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
            throw new ShareException(ExceptionCode.pay_type_error);
        }
        return usdPreBtc;
    }
}
