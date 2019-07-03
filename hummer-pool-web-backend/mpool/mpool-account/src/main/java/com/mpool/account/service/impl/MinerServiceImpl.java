package com.mpool.account.service.impl;

import com.mpool.account.mapper.miner.MinerMapper;
import com.mpool.account.service.MinerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MinerServiceImpl implements MinerService {
    @Autowired
    private MinerMapper minerMapper;
    @Autowired
    private RestTemplate restTemplate;


    @Override
    public List<Map<String, Object>> getMinerListByCurrencyId(int currencyId) {
        List<Map<String, Object>> result = minerMapper.findMinerByCurrencyId(currencyId);
        for(Map<String, Object> list : result){
            list.put("path", "");
            list.put("color", "green");
        }
        return result;
    }

    @Override
    public Double getCurBtcPriceToUsd() {
        String btcPreUsd = restTemplate.getForObject("https://blockchain.info/tobtc?currency=USD&value=1", String.class);
        Double usdPreBtc = 1.0D / (Double.parseDouble(btcPreUsd));
        log.debug("getCurBtcPriceToUsd result= {}, usdPreBtc= {}", btcPreUsd, usdPreBtc);
        return usdPreBtc;
    }
}
