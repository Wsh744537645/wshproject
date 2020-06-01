package com.dubbo.consumer;

import com.dubbo.provider.service.ProviderService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 参照资料：https://segmentfault.com/a/1190000019896723
 * @author jmfen
 * date 2020-06-01
 */
public class ConsumerApplicationXml {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("consumer.xml");
        context.start();
        ProviderService providerService = (ProviderService) context.getBean("providerService");
        String str = providerService.sayHello("heheda");
        System.out.println(str);
        System.in.read();
    }
}