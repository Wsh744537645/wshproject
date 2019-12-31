package com.springmvc;

import com.springmvc.base.MyController;
import com.springmvc.base.MyRequestMapping;

/**
 * @author jmfen
 * date 2019-12-30
 */

@MyController
@MyRequestMapping("/testController")
public class TestController {

    @MyRequestMapping("/test")
    public String test(){
        System.out.println("执行test业务");
        return "index";
    }
}