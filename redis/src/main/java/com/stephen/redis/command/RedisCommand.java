package com.stephen.redis.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wushuihua
 * date 2019-07-26
 */

@Component
public class RedisCommand implements CommandLineRunner {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void run(String... args) throws Exception {
//        Map<String, Object> map = new HashMap<>();
//        map.put("key1", "1");
//        map.put("key2", "2");
//        map.put("key3", "3");
//
//        redisTemplate.opsForHash().putAll("hash_key_1", map);

        //redisTemplate.opsForHash().put("hash_key_2", "wsh_key", "4");

        Object value = redisTemplate.opsForHash().get("hash_key_2", "wsh_key");
        System.out.println("value= " + value);
    }
}