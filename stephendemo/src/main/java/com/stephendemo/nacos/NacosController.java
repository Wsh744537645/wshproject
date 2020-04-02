package com.stephendemo.nacos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jmfen
 * date 2020-04-01
 */
@RestController
public class NacosController {

    @Autowired
    private NacosService nacosService;

    @GetMapping("/nacos/serverName")
    public String getServerName(){
        return nacosService.getServerName();
    }
}