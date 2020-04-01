package com.stephendemo;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.stephendemo.beandefinition.BeanDef;
import com.stephendemo.fruit.Apple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;

import javax.annotation.PostConstruct;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
//@EnableDiscoveryClient
@Slf4j
public class StephendemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(StephendemoApplication.class, args);

        BeanDef beanDef = new BeanDef(context);
        beanDef.showBeanDef();

        Apple apple = context.getBean(Apple.class);
        log.info("------------fruit = {}", apple);

//        synchronized (StephendemoApplication.class){
//            try {
//                StephendemoApplication.class.wait();
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
    }

    @EventListener(WebServerInitializedEvent.class)
    public void onWebServerReady(WebServerInitializedEvent event){
        System.out.println("当前WebServer实现类为：" + event.getWebServer().getClass().getName());
    }

}
