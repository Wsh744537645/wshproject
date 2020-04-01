package com.stephendemo.proxy;

import com.stephendemo.proxy.jdk.Animal;
import com.stephendemo.proxy.jdk.AnimalProxy;
import com.stephendemo.proxy.jdk.Cat;

import java.util.Random;

/**
 * @author jmfen
 * date 2020-03-17
 */
public class JdkProxyDemo {
    public static void main(String[] args) {
        AnimalProxy proxy =  new AnimalProxy();
        Animal animal = proxy.getInstance(new Cat());
        animal.eat();
        System.out.println(animal.getClass().getName());
        Random r = new Random();
        for (int i = 0; i<10; i++) {
            System.out.println(r.nextInt(3));
        }
    }
}