package com.stephendemo.jucaqs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author jmfen
 * date 2020-04-18
 */
public class ThreadPoolExecutorDemo {
    public static void main(String[] args) {
        int core = 3;
        int max = 5;
        int queueLength = 1;
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(core, max, 1, TimeUnit.MINUTES, new LinkedBlockingQueue(queueLength));

        Callable<Integer> callable = ()->{
            System.out.println(Thread.currentThread().getName() + " come on wait...");
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " will done...");
            return 1;
        };
        List<Future<Integer>> list = new ArrayList<>();
        for(int i = 1; i <= core + 3; i++){
            Future<Integer> result = threadPool.submit(callable);
            list.add(result);
        }

        for(Future future : list){
            try {
                System.out.println(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        System.out.println(threadPool);
    }
}