package com.mpool.rpc.account.exception;

import com.mpool.rpc.account.producer.AccountProducerApi;
import org.springframework.stereotype.Component;

@Component
public class AccountFeignException implements AccountProducerApi {

    @Override
    public String getMultiple(){
        return "服务发生故障";
    }
}
