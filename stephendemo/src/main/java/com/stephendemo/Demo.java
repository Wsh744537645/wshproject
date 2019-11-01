package com.stephendemo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jmfen
 * date 2019-10-21
 */
public class Demo {
    public static void main(String[] args){
        new Thread(()->{
            List<byte[]> list = new ArrayList<>();
            while (true){
                byte[] bytes = new byte[1024 * 1024];
                list.add(bytes);
                System.out.println(new Date().toString() + Thread.currentThread().getName());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(()->{
            while (true) {
                System.out.println(new Date().toString() + Thread.currentThread().getName());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}