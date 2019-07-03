package com.mpool.account.model.miner;

import lombok.Data;

/**
 * 矿机信息
 */

@Data
public class MinerModel {
    private int miner_id;
    private String chip;
    private String miner_type;
    private int share;
    private int power_waste;
}
