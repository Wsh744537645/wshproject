package com.stephendemo.jucaqs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author jmfen
 * date 2020-04-03
 */
public class CountDownDemo {

    public static void main(String[] args) {
        CountDownLatch downLatch = new CountDownLatch(5);
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(1));
        for(int i = 1; i <= 5; i++){
            poolExecutor.execute(() -> {
                System.out.println("线程 " + Thread.currentThread().getName() + " 来了");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                downLatch.countDown();
                System.out.println("线程 " + Thread.currentThread().getName() + " finaly");
            });
        }

        System.out.println("main thread 1");
        try {
            downLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main final");
    }
}