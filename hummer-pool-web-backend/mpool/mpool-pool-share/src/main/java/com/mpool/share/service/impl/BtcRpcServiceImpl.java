package com.mpool.share.service.impl;

import com.googlecode.jsonrpc4j.Base64;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.mpool.share.config.BtcRpcConfig;
import com.mpool.share.mapper.product.WalletAddressMapper;
import com.mpool.share.service.BtcRpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @Author: stephen
 * @Date: 2019/5/27 10:39
 */

@Service
@Slf4j
public class BtcRpcServiceImpl implements BtcRpcService {

    @Autowired
    private BtcRpcConfig btcRpcConfig;
    @Autowired
    private WalletAddressMapper walletAddressMapper;
    @Autowired
    private ExecutorService executorService;

    private JsonRpcHttpClient client = null;

//    public BtcRpcServiceImpl(){
//        initClient();
//    }

    private void initClient(){
        if(client == null) {
            //身份认证
            String cred = Base64.encodeBytes((btcRpcConfig.getUser() + ":" + btcRpcConfig.getPassword()).getBytes());
            Map<String, String> headers = new HashMap<>(1);
            headers.put("Authorization", "Basic " + cred);
            try {
                client = new JsonRpcHttpClient(new URL("http://" + btcRpcConfig.getAllowip() + ":" + btcRpcConfig.getPort()), headers);
            } catch (MalformedURLException e) {
                log.error("json rpc connect to bitcoind fail......");
                e.printStackTrace();
            }
        }
    }

    @Override
    public Double GetBalance(String payAddress) {
        initClient();
        Double balance = new Double(0D);
        int count = 5;
        while (true) {
            try {
                balance = client.invoke("getreceivedbyaddress", new Object[]{payAddress, 6}, Double.class);
                break;
            } catch (Throwable throwable) {
                log.error("【btc余额查询失败】 address={}", payAddress);
                if(count <= 0){
                    break;
                }
            }
            count--;
        }
        return balance;
    }

    @Override
    public Long getDifficult() {
        initClient();
        Long diff = new Long(7459680720542L);
        int count = 5;
        while (true) {
            try {
                diff = client.invoke("getdifficulty", new Object[]{}, Long.class);
                break;
            } catch (Throwable throwable) {
                log.error("【查询全网难度失败】");
                if(count <= 0){
                    break;
                }
            }
            count--;
        }
        return diff;
    }

    @Override
    public String getNewWalletAddress() {
        initClient();

        String address = null;
        try {
            address = client.invoke("getnewaddress", new Object[]{}, String.class);
        } catch (Throwable throwable) {
            log.error("【新建收款钱包地址失败】");
        }
        return address;
    }

    @Override
    public void createWalletAddressBatch() {
        initClient();
        executorService.execute(() -> {
                List<String> list = new ArrayList<>(100);
                for(int i = 0; i < 100; i++){
                    String address = null;
                    try {
                        address = client.invoke("getnewaddress", new Object[]{}, String.class);
                    } catch (Throwable throwable) {
                        log.error("【新建收款钱包地址失败】");
                    }
                    if(address != null) {
                        list.add(address);
                    }
                }

                walletAddressMapper.inserts(list);
        });
    }
}
