package com.stephendemo.controller;

import com.stephendemo.RedisOperateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author jmfen
 * date 2019-10-18
 */

@RestController
public class DemoController {
    @Autowired
    //@Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

//    @Autowired
//    @Qualifier("redisTemplate2")
//    private RedisTemplate redisTemplate2;

    @Autowired
    private RedisOperateService redisOperateService;

    public DemoController(){
        System.out.println("demo controller");
    }

    @PostConstruct
    public void init(){
        System.out.println("spring ready........");
    }

    @PreDestroy
    public void destory(){
        System.out.println("spring destory");
    }

    @GetMapping("/test/dead")
    public String demoTest1(){
        while (true){
            if(System.currentTimeMillis() < 1){
                break;
            }
        }
        return "hello dead";
    }

    @GetMapping("/test/hello")
    public String demoTest2(){
        return "hello java...";
    }

    @GetMapping("/redis/value")
    public Object redisDemo(@RequestParam Integer type){
        if(type.equals(1)) {
            redisOperateService.saveByZset();
        }else if(type.equals(2)){
            redisOperateService.operateZset();
        }else if(type.equals(3)) {
            for(int i = 0; i < 4; i++){
                new Thread("t" + i){
                    @Override
                    public void run(){
                        while (true) {
                            boolean lock = redisOperateService.getLock("lock_key", 18);
                            System.out.println(Thread.currentThread().getName() + " lock " + lock);
                            if (lock) {
                                System.out.println(Thread.currentThread().getName() + " do some thing....");
                                try {
                                    TimeUnit.SECONDS.sleep(4);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                System.out.println(Thread.currentThread().getName() + " finised..");
                                System.out.println("unlock do result "+ redisOperateService.unlock("lock_key"));
                                break;
                            } else {
                                try {
                                    System.out.println(Thread.currentThread().getName() + " sleep..");
                                    TimeUnit.SECONDS.sleep(6);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }.start();
            }
        }
        return "hehe";
    }
}