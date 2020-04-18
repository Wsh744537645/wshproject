package com.stephendemo.jucaqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author jmfen
 * date 2020-04-16
 */
public class ReentrantReadWriteLockDemo {
    public static void main(String[] args) {
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        for(int i = 0; i < 10; i++){
            new Thread(()->{
                try {
                    TimeUnit.SECONDS.sleep(1);
                    readWriteLock.readLock().lock();
                    System.out.println(Thread.currentThread().getName() + "---> require read lock ....");
                    TimeUnit.SECONDS.sleep(1);
                }catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    readWriteLock.readLock().unlock();
                }
            }, "thread_read_" + i).start();
        }

        new Thread(()->{
            try {
                readWriteLock.writeLock().lock();
                System.out.println(Thread.currentThread().getName() + "---> require write lock ....");
                TimeUnit.SECONDS.sleep(5);
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                readWriteLock.writeLock().unlock();
            }
        }, "thread_write").start();
    }
}