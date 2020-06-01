package com.dubbo.provider.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注解方式配置
 * @author jmfen
 * date 2020-06-01
 */

@Configuration
@EnableDubbo(scanBasePackages = "com.dubbo.provider.service")
public class DubboConfiguration {

    //服务提供者信息配置
    @Bean
    public ProviderConfig providerConfig(){
        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setTimeout(1000);
        return providerConfig;
    }

    //应用信息配置
    @Bean
    public ApplicationConfig applicationConfig(){
        return new ApplicationConfig("dubbo-annotation-provider");
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

    //使用协议配置，这里使用 dubbo
    @Bean
    public ProtocolConfig protocolConfig(){
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(20880);
        return protocolConfig;
    }

}