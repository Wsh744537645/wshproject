package com.stephendemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author jmfen
 * date 2020-03-18
 */

@Service
@Slf4j
public class RedisOperateService {

    @Autowired
    private RedisTemplate redisTemplate;

    private final String key = "phones1";
    private final String lockKey = "lock_key";

    public void saveByZset(){
        ZSetOperations.TypedTuple<Object> typedTuple1 = new DefaultTypedTuple<>("xiaomi1", 900D);
        ZSetOperations.TypedTuple<Object> typedTuple2 = new DefaultTypedTuple<>("huawei1", 900D);
        ZSetOperations.TypedTuple<Object> typedTuple3 = new DefaultTypedTuple<>("oppo1", 900D);
        ZSetOperations.TypedTuple<Object> typedTuple4 = new DefaultTypedTuple<>("apple1", 900D);
        Set<ZSetOperations.TypedTuple<Object>> set = new HashSet<>();
        set.add(typedTuple1);
        set.add(typedTuple2);
        set.add(typedTuple3);
        set.add(typedTuple4);
        redisTemplate.opsForZSet().add(key, set);
    }

    public void operateZset(){
        log.info("range [0,-1] = {}", redisTemplate.opsForZSet().range(key, 0, -1));
        log.info("range [0,2] = {}", redisTemplate.opsForZSet().range(key, 0, 2));
        log.info("rangeByScore [1000,2000] = {}", redisTemplate.opsForZSet().rangeByScore(key, 1000, 2000));

        RedisZSetCommands.Range range = new RedisZSetCommands.Range();
        //range.gt("apple1");
        range.lt("c");
        log.info("rangbylex -- {}", redisTemplate.opsForZSet().rangeByLex(key, range));
    }

    /**
     * 1.这里的connect是redis原生链接，所以connection的返回结果是基本上是byte数组，如果需要存储的数据，需要对byte[]数组反序列化。
     * 2.在doInRedis中返回值必须返回为null，为什么返回为空？可以定位到内部代码去查看详情。
     * 3.connection.openPipeline()可以调用，也可以不调用，但是connection.closePipeline()不能调用，调用了拿不到返回值。因为调用的时候会直接将结果返回，同时也不会对代码进行反序列化。
     * 4.反序列化需要传入反序列化对象，这些对象都可以进行相应的实例化
     * @return
     */
    public List<Long> pipeline(){
        return redisTemplate.executePipelined((RedisCallback<Long>)connection -> {
            connection.openPipeline();
            for (int i = 0; i < 1000000; i++) {
                String key = "123" + i;
                connection.zCount(key.getBytes(), 0,Integer.MAX_VALUE);
            }
            return null;
        });
    }

    public boolean getLock(String lockKey, long lockExpireSec){
        Boolean result = (Boolean) redisTemplate.execute((RedisCallback)  connection -> {
            long nowTime = System.currentTimeMillis();
            long value = nowTime + lockExpireSec * 1000 + 1;
            Boolean acquire = connection.set(lockKey.getBytes(), String.valueOf(value).getBytes(), Expiration.seconds(lockExpireSec), RedisStringCommands.SetOption.SET_IF_ABSENT);
            if(acquire != null && acquire.equals(Boolean.TRUE)){
                return Boolean.TRUE;
            }else {
                byte[] bytes = connection.get(lockKey.getBytes());
                if(bytes != null && bytes.length > 0) {
                    long lockValue = Long.parseLong(new String(bytes));
                    if (nowTime > lockValue) {
                        byte[] oldValue = connection.getSet(lockKey.getBytes(), String.valueOf(value).getBytes());
                        return oldValue != null && Long.parseLong(new String(oldValue)) == lockValue;
                    }
                }
            }
            return Boolean.FALSE;
        });
        return result != null && result.equals(Boolean.TRUE);
    }

    public Long unlock(String lockKey){
        return redisTemplate.getConnectionFactory().getConnection().del(lockKey.getBytes());
    }
}