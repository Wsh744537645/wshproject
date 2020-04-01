package com.stephendemo.proxy.jdk;

/**
 * @author jmfen
 * date 2020-03-17
 */
public class Dog implements Animal {

    @Override
    public void eat() {
        System.out.println("dog eat!!!");
    }
}