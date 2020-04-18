package com.stephendemo.jucaqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jmfen
 * date 2020-04-16
 */
public class ReentrantLockDemo {

    private ReentrantLock lock = new ReentrantLock(true);

    private void lockTest(){
        try {
            System.out.println(Thread.currentThread().getName() + "come on");
            lock.lock();
//            lock.newCondition().await();
//            lock.newCondition().signal();
            System.out.println(Thread.currentThread().getName() + " ower lock......");
            TimeUnit.MILLISECONDS.sleep(50);
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ReentrantLockDemo demo = new ReentrantLockDemo();
        Runnable runnable = () -> {
            demo.lockTest();
        };

        for(int i = 1; i <= 50; i++){
            new Thread(runnable, "thread" + i).start();
        }
    }
}