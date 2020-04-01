package com.stephendemo.nacos;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jmfen
 * date 2020-04-01
 */
@NacosPropertySource(dataId = "wsh.test", autoRefreshed = true)
@RestController
public class NacosController {

    @NacosValue(value = "${service.name:1}", autoRefreshed = true)
    private String serverName;

    @GetMapping("/nacos/serverName")
    public String getServerName(){
        return serverName;
    }
}