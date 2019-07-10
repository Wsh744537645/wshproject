package com.kafka.producer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: stephen
 * @Date: 2019/7/10 11:39
 */

@RestController
@RequestMapping("/p")
public class ProductController {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/send")
    public String send(String msg){
        kafkaTemplate.send("test_topic", msg);
        return "success send msg: " + msg;
    }

}
