package com.stephendemo.nacos;

import com.alibaba.nacos.api.config.listener.AbstractListener;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.alibaba.nacos.NacosConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jmfen
 * date 2020-04-02
 */

@Configuration
public class DemoNacosConfiguration {

    @Autowired
    private NacosConfigProperties nacosConfigProperties;

    @Bean
    public String demoConfiguration(){
        String data_id = "demo_json";
        String group = "DEFAULT_GROUP";
        try {
            String str = nacosConfigProperties.configServiceInstance().getConfig(data_id, group, 10 * 1000);
            nacosConfigProperties.configServiceInstance().addListener(data_id, group, new AbstractListener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    System.out.println("nacos update demo_json: " + configInfo);
                }
            });
            return str;
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return "no_data";
    }
}