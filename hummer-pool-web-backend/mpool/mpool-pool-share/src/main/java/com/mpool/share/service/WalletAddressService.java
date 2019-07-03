package com.mpool.share.service;

import com.mpool.common.model.product.WalletAddressModel;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/23 16:00
 */
public interface WalletAddressService {
    WalletAddressModel getValidWalletAddress();

    void updateEnabledBatch(List<String> walletAddressList, Integer state);

    List<WalletAddressModel> getHadBalanceWalletAddressList();
}
