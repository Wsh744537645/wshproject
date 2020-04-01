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