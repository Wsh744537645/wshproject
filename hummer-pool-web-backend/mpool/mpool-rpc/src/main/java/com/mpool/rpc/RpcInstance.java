package com.mpool.rpc;

import com.mpool.rpc.account.producer.btc.MultipleShare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: stephen
 * @Date: 2019/5/6 15:35
 */

@Component
public class RpcInstance {
    @Autowired
    private MultipleShare zcrashInstance;

    public MultipleShare getRpcInstanceByCurrencyId(Integer id){
        if(id.equals(2)){
            return zcrashInstance;
        }

        return null;
    }

    public MultipleShare getRpcInstanceByCurrencyType(String type){
        if(type.equals("ZEC")){
            return zcrashInstance;
        }

        return null;
    }
}
