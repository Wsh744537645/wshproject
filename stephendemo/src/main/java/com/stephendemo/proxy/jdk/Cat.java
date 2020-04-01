package com.stephendemo.proxy.jdk;

/**
 * @author jmfen
 * date 2020-03-17
 */
public class Cat implements Animal {

    @Override
    public void eat() {
        System.out.println("cat eat!!!");
    }
}