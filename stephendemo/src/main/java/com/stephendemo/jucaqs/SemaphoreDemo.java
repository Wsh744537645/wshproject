package com.stephendemo.jucaqs;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author jmfen
 * date 2020-05-14
 */
public class SemaphoreDemo {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(5);
        for(int i = 0; i < 20; i++){
            final int no = i + 1;
            Runnable runnable = () -> {
                try {
                    semaphore.acquire();
                    System.out.println("accept no: " + no);
                    TimeUnit.SECONDS.sleep((long)Math.random() + 10);
                    semaphore.release();
                    System.out.println("* " + semaphore.availablePermits());
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            };
            executorService.execute(runnable);
        }
        executorService.shutdown();
    }
}