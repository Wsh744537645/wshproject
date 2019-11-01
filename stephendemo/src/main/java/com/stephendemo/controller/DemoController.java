package com.stephendemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author jmfen
 * date 2019-10-18
 */

@RestController
public class DemoController {
    @Autowired
    @Qualifier("redisTemplate1")
    private RedisTemplate redisTemplate;

    @Autowired
    @Qualifier("redisTemplate2")
    private RedisTemplate redisTemplate2;


    @GetMapping("/test/dead")
    public String demoTest1(){
        while (true){
            if(System.currentTimeMillis() < 1){
                break;
            }
        }
        return "hello dead";
    }

    @GetMapping("/test/hello")
    public String demoTest2(){
        return "hello java...";
    }

    @GetMapping("/redis/value")
    public Object redisDemo(String value){
        redisTemplate.opsForValue().set(value, value);
        return redisTemplate2.opsForValue().get(value);
    }
}