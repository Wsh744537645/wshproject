package com.dubbo.provider.service;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 注解方式实现类
 * @author jmfen
 * date 2020-06-01
 */

@Service(timeout = 500)
public class ProviderServiceImplAnnotation implements ProviderServiceAnnotation {
    @Override
    public String SayHelloAnnotation(String word) {
        return word;
    }
}