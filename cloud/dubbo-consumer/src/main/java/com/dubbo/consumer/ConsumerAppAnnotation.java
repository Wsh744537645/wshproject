package com.dubbo.consumer;

import com.dubbo.consumer.config.ConsumerConfiguration;
import com.dubbo.consumer.service.ConsumerAnnotationService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author jmfen
 * date 2020-06-01
 */
public class ConsumerAppAnnotation {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConsumerConfiguration.class);
        context.start();
        ConsumerAnnotationService service = context.getBean(ConsumerAnnotationService.class);
        String result = service.doSayHello("hi annotation");
        System.out.println("result: " + result);
    }
}