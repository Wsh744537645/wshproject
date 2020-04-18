package com.stephendemo.jucaqs;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jmfen
 * date 2020-04-03
 */
public class LockDemo {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();

        for(int i = 0; i < 10 ; i++){
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " try lock");
                lock.lock();
                try {
                    int second = new Random().nextInt(5);
                    System.out.println(Thread.currentThread().getName() + " sleep " + second);
                    TimeUnit.SECONDS.sleep(second);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    System.out.println(Thread.currentThread().getName() + " unlock ");
                    lock.unlock();
                }
            }).start();
        }
    }
}