package com.dubbo.consumer;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.dubbo.provider.service.ProviderService;

import java.io.IOException;

/**
 * api的方式调用
 * api的方式调用不需要其他的配置，只需要下面的代码即可。
 * 但是需要注意，官方建议：
 * Api方式用于测试用例使用，推荐xml的方式
 * @author jmfen
 * date 2020-06-01
 */
public class ConsumerApplicationApi {

    public static void main(String[] args) throws IOException {
        //当前应用配置
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("consumer");
        applicationConfig.setOwner("wsh");

        //连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setProtocol("zookeeper");
        registry.setAddress("192.168.146.129");
        registry.setPort(2181);

        // 注意：ReferenceConfig为重对象，内部封装了与注册中心的连接，以及与服务提供方的连接
        // 引用远程服务
        ReferenceConfig<ProviderService> reference = new ReferenceConfig<>();
        reference.setApplication(applicationConfig);
        reference.setRegistry(registry);
        reference.setInterface(ProviderService.class);

        //和本地bean一样使用xxxService
        ProviderService providerService = reference.get();
        System.out.println(providerService.sayHello("im wsh, hello dubbo"));

        System.in.read();
    }
}