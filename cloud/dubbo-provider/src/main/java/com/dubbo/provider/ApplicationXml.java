package com.dubbo.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 参照资料：https://segmentfault.com/a/1190000019896723
 * @author jmfen
 * date 2020-05-30
 */
public class ApplicationXml {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("METE-INF/spring/provider.xml");
        context.start();
        System.in.read();
    }
}