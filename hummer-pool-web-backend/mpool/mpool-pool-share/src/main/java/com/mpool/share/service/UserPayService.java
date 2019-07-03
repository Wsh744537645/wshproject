package com.mpool.share.service;

/**
 * @Author: stephen
 * @Date: 2019/5/29 20:41
 */
public interface UserPayService {
    String getUserWalletAddress();

    Double getUserBalance(Long userId);

    /**
     * 获得用户返佣总收益
     * @param userId
     * @return
     */
    Double getRecommendTotal(Long userId);
}
