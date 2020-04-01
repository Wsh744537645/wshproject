package com.stephendemo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author jmfen
 * date 2020-03-09
 */
public class WorkStealingPoolTest {
    // 线程数
    private static final int threads = 10;
    // 用于计数线程是否执行完成
    CountDownLatch countDownLatch = new CountDownLatch(threads);

    /**
     * newFixedThreadPool execute
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void test1() throws ExecutionException, InterruptedException {
        System.out.println("---- start ----");
        ExecutorService executorService = Executors.newWorkStealingPool();
        for (int i = 0; i < threads; i++) {
            executorService.execute(() -> {
                try {
                    System.out.println(Thread.currentThread().getName());
                } catch (Exception e) {
                    System.out.println(e);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        System.out.println("---- end ----");
    }

    /**
     * newFixedThreadPool submit submit
     */
    public void test2() throws InterruptedException {
        System.out.println("---- start ----");
        ExecutorService executorService = Executors.newWorkStealingPool();
        for (int i = 0; i < threads; i++) {
//            Callable 带返回值
            executorService.submit(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            }));
        }
        countDownLatch.await();
        System.out.println("---- end ----");
    }
    /**
     * newFixedThreadPool submit Callable
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void test3() throws ExecutionException, InterruptedException {
        System.out.println("---- start ----");
        ExecutorService executorService = Executors.newWorkStealingPool();
        for (int i = 0; i < threads; i++) {
//          Runnable 带返回值
            FutureTask<?> futureTask = new FutureTask<>(new Callable<String>() {
                /**
                 * call
                 * @return currentThreadName
                 */
                @Override
                public String call() {
                    return Thread.currentThread().getName();
                }
            });
            executorService.submit(new Thread(futureTask));
            System.out.println(futureTask.get());
        }
        System.out.println("---- end ----");
    }

    public static void main(String[] args) {
        Map<String, String> map = new ConcurrentHashMap<>();
        map.put("t", "test");
        map.put("t", "test1");
        map.get("t");
        System.out.println(map.size());

        Map<String, String> map1 = new HashMap<>();
        map1.put("t", "test");

        try {
            new WorkStealingPoolTest().test1();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}