package com.stephendemo.jucaqs;

import java.util.concurrent.*;

/**
 * @author jmfen
 * date 2020-04-03
 */
public class CyclicBarrierDemo {
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(5, () -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(1));
        for(int i = 1; i <= 5; i++){
            poolExecutor.execute(() -> {
                try {
                    int second = ThreadLocalRandom.current().nextInt(2, 5);
                    System.out.println("线程 " + Thread.currentThread().getName() + " 来了, 休眠 " + second);
                    TimeUnit.SECONDS.sleep(second);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("线程 " + Thread.currentThread().getName() + " finaly");
            });
        }
    }
}