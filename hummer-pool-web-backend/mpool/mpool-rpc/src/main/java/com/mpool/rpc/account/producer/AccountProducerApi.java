package com.mpool.rpc.account.producer;

import com.mpool.rpc.UrlConfig;
import com.mpool.rpc.account.exception.AccountFeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = UrlConfig.MULTIPLE_SERVICE_NAME_ZCRASH, url = UrlConfig.MULTIPLE_SERVICE_ZCRASH, fallback = AccountFeignException.class)
public interface AccountProducerApi {
    @GetMapping("multiple/get")
    String getMultiple();
}
