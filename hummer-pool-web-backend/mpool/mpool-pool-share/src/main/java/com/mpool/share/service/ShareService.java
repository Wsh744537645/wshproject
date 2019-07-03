package com.mpool.share.service;

/**
 * @Author: stephen
 * @Date: 2019/5/27 17:36
 */
public interface ShareService {

    /**
     * 生成每日收益系数
     */
    void taskShareRate();

    /**
     * 生成每日收益账单
     */
    void taskSettlement();

    /**
     * 处理每日账单到余额
     */
    void taskBalance();

    /**
     * 检测收款地址的余额是否被转出，如果被转出则回收
     */
    void checkWalletAddress();
}
