package com.dubbo.provider;

import com.dubbo.provider.config.DubboConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

/**
 * 注解启动方式
 * @author jmfen
 * date 2020-06-01
 */
public class AppAnnotation {

    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(DubboConfiguration.class);
        applicationContext.start();
        System.in.read();
    }
}