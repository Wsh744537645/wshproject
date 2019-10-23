package com.stephendemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jmfen
 * date 2019-10-18
 */

@RestController
public class DemoController {

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
}