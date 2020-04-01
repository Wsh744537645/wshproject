package com.stephendemo;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author jmfen
 * date 2019-10-21
 */
public class ThreadLocalDemo {

    private static ThreadLocal<String> user;

    public static void main(String[] args){
        user = ThreadLocal.withInitial(() -> "wsh");
        for(int i = 1; i < 10; i++){
            new Thread("t" + i){
                @Override
                public void run() {
                    System.out.println(user.get());
                }
            }.start();
        }
    }
}