package com.dubbo.provider;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.dubbo.provider.service.ProviderService;
import com.dubbo.provider.service.ProviderServiceImpl;

import java.io.IOException;

/**
 * Api方式启动
 * api的方式调用不需要其他的配置，只需要下面的代码即可。
 * 但是需要注意，官方建议：
 * Api方式用于测试用例使用，推荐xml的方式
 * @author jmfen
 * date 2020-06-01
 */
public class ApplicationApi {

    public static void main(String[] args) throws IOException {
        //服务实现
        ProviderService providerService = new ProviderServiceImpl();

        //当前应用配置
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("provider");
        applicationConfig.setOwner("wsh");

        //连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setProtocol("zookeeper");
        registry.setAddress("192.168.146.129");
        registry.setPort(2181);
        //registry.setUsername("aaa");
        //registry.setPassword("bbb");

        //服务提供者协议配置
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(20880);
        //protocolConfig.setThreads(200);

        //服务提供者暴露服务配置,PS:此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
        ServiceConfig<ProviderService> service = new ServiceConfig<>();
        service.setApplication(applicationConfig);
        // 多个注册中心可以用setRegistries()
        service.setRegistry(registry);
        // 多个协议可以用setProtocols()
        service.setProtocol(protocolConfig);
        service.setInterface(ProviderService.class);
        service.setRef(providerService);
        service.setVersion("1.0.0");

        //暴露及注册服务
        service.export();

        System.in.read();
    }
}