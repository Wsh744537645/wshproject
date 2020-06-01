package com.dubbo.provider.service;

/**
 * @author jmfen
 * date 2020-05-30
 */
public class ProviderServiceImpl implements ProviderService {

    @Override
    public String sayHello(String word) {
        System.out.println("param: " + word);
        return "Hello: " + word;
    }
}