package com.mpool.share.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpool.common.model.product.WalletAddressModel;
import com.mpool.share.exception.ExceptionCode;
import com.mpool.share.exception.ShareException;
import com.mpool.share.mapper.product.WalletAddressMapper;
import com.mpool.share.service.BtcRpcService;
import com.mpool.share.service.WalletAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/23 16:01
 */

@Service
@Slf4j
public class WalletAddressServiceImpl implements WalletAddressService {
    @Autowired
    private WalletAddressMapper walletAddressMapper;
    @Autowired
    private BtcRpcService rpcService;

    @Override
    public synchronized WalletAddressModel getValidWalletAddress() {
        List<WalletAddressModel> walletAddressModels = walletAddressMapper.getOne();
        if(walletAddressModels == null || walletAddressModels.isEmpty()){
            String newAddress = rpcService.getNewWalletAddress();
            if(newAddress == null){
                log.error("【收款地址获取失败】 没有可用的收款地址");
                throw new ShareException(ExceptionCode.param_ex);
            }
            WalletAddressModel model = new WalletAddressModel();
            model.setEnabled(0);
            model.setWalletAddress(newAddress);
            walletAddressMapper.insert(model);

            //批量生成一批收款地址
            rpcService.createWalletAddressBatch();
            return  model;
        }

        //修改收款钱包地址的enabled为0，即设置不可用状态
        walletAddressMapper.updateEnabled(walletAddressModels.get(0).getId(), 0);

        return walletAddressModels.get(0);
    }

    @Override
    public void updateEnabledBatch(List<String> walletAddressList, Integer state) {
        walletAddressMapper.updateEnabledBatch(walletAddressList, state);
    }

    @Override
    public List<WalletAddressModel> getHadBalanceWalletAddressList() {
        return walletAddressMapper.getHadBalanceWalletAddressList();
    }
}
