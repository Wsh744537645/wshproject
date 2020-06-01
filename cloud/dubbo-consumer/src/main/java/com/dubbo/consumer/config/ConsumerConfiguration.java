package com.dubbo.consumer.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jmfen
 * date 2020-06-01
 */

@Configuration
@EnableDubbo(scanBasePackages = "com.dubbo.consumer.service")
@ComponentScan(value = "com.dubbo.consumer.service")
public class ConsumerConfiguration {

    //应用配置
    @Bean
    public ApplicationConfig applicationConfig(){
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("dubbo-annotation-consumer");
        Map<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put("qos.enable","true");
        stringStringMap.put("qos.accept.foreign.ip","false");
        stringStringMap.put("qos.port","33333");
        applicationConfig.setParameters(stringStringMap);
        return applicationConfig;
    }

    //服务消费者配置
    @Bean
    public ConsumerConfig consumerConfig(){
        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setTimeout(3000);
        return consumerConfig;
    }

    //注册中心信息配置
    @Bean
    public RegistryConfig registryConfig(){
        RegistryConfig registry = new RegistryConfig();
        registry.setProtocol("zookeeper");
        registry.setAddress("192.168.146.129");
        registry.setPort(2181);
        return registry;
    }
}