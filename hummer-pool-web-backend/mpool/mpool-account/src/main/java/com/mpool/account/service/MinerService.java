package com.mpool.account.service;

import java.util.List;
import java.util.Map;

public interface MinerService {
    List<Map<String, Object>> getMinerListByCurrencyId(int currencyId);

    /**
     * 获得当前1个比特币的美元价格
     * @return
     */
    Double getCurBtcPriceToUsd();
}
