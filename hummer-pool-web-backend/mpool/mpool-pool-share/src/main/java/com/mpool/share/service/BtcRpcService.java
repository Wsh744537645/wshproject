package com.mpool.share.service;

/**
 * @Author: stephen
 * @Date: 2019/5/27 10:38
 */
public interface BtcRpcService {

    Double GetBalance(String payAddress);

    /**
     * 获得难度
     * @return
     */
    Long getDifficult();

    /**
     * 获得新的收款钱包地址
     * @return
     */
    String getNewWalletAddress();

    /**
     * 批量产生收款地址
     */
    void createWalletAddressBatch();
}
