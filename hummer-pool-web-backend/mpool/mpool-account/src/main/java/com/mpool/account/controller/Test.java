package com.mpool.account.controller;


import com.mpool.rpc.account.producer.AccountProducerApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;


@RequestMapping("/wsh")
@Controller
public class Test {

    @Autowired
    private AccountProducerApi accountProducerApi = null;

    @GetMapping("/getMu")
    @ResponseBody
    public String getMu(){
//        String url = "http://127.0.0.1:9000/multiple/get";
//        RestTemplate restTemplate = new RestTemplate();
//        String result = restTemplate.getForObject(url, String.class);

        String result = accountProducerApi.getMultiple();

        return  result;
    }
}
