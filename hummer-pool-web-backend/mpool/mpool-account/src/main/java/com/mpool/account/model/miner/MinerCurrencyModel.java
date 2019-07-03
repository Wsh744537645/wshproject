package com.mpool.account.model.miner;

import lombok.Data;

@Data
public class MinerCurrencyModel {
    private int id;
    private int currency_id;
    private String miner_type;
}
