package com.springcloud.product.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jmfen
 * date 2020-05-12
 */

@RestController
public class ProductController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/query/info")
    public String query(){
        if(port == null){
            return "aaa";
        }
        return port.equals("9990") ? "aaa1" : "aaa2";
    }
}