package com.stephendemo.jucaqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author jmfen
 * date 2020-04-16
 */
public class LockSupportDemo {
    public static void main(String[] args) {
        //test1();
        //test2();
        //test3();
        //test4();
        test5();
    }

    /**
     * LockSupport.park() 阻塞
     */
    static void test1(){
        System.out.println("test 1");
        LockSupport.park();
        System.out.println("test 2");
    }

    /**
     * 先释放许可，再获取许可，主线程能够正常终止。
     * LockSupport许可的获取和释放，一般来说是对应的，如果多次unpark，只有一次park也不会出现什么问题，结果是许可处于可用状态。
     */
    static void test2(){
        LockSupport.unpark(Thread.currentThread());
        System.out.println("test 1");
        LockSupport.park();
        System.out.println("test 2");
    }

    /**
     * LockSupport 是不可重入的
     */
    static void test3(){
        LockSupport.unpark(Thread.currentThread());
        System.out.println("test 1");
        LockSupport.park();
        System.out.println("test 2");
        LockSupport.park();
        System.out.println("test 3");
    }

    /**
     * LockSupport 是不可重入的
     * 可以执行 unpark() ，但是 park() 只能执行一次，否则会处理阻塞、死锁
     */
    static void test4(){
        LockSupport.unpark(Thread.currentThread());
        LockSupport.unpark(Thread.currentThread());
        System.out.println("test 1");
        LockSupport.park();
        System.out.println("test 2");
        LockSupport.park();
        System.out.println("test 3");
    }

    /**
     * LockSupport对应中断的响应性.
     * 线程如果因为调用park而阻塞的话，能够响应中断请求(中断状态被设置成true)，但是不会抛出InterruptedException。
     */
    static void test5(){
        Thread thread = new Thread(()->{
            int count = 0;
            long time = System.currentTimeMillis();
            long end = 0;
            while (end - time < 1000){
                end = System.currentTimeMillis();
                count++;
            }
            System.out.println("after 1 second, count=" + count);

            LockSupport.park();

            System.out.println(Thread.currentThread().getName() + " Over.... " + Thread.currentThread().isInterrupted());
        }, "thread A");

        thread.start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //LockSupport.unpark(thread);
        thread.interrupt();

        System.out.println("main thread over...");
    }
}