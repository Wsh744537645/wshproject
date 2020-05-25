package com.stephendemo.web_server_demo.demo2;

import java.io.IOException;

/**
 * 提供静态资源请求
 * @author jmfen
 * date 2020-05-22
 */
public class StaticResourceProcessor {

    public void process(ServletRequestDemo1 request, ServletResponseDemo1 response){
        try {
            response.sendStaticResource();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}