package com.dubbo.consumer.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dubbo.provider.service.ProviderServiceAnnotation;
import org.springframework.stereotype.Component;

/**
 * @author jmfen
 * date 2020-06-01
 */

@Component("annotatedConsumer")
public class ConsumerAnnotationService {

    @Reference
    private ProviderServiceAnnotation providerServiceAnnotation;

    public String doSayHello(String name){
        return providerServiceAnnotation.SayHelloAnnotation(name);
    }
}