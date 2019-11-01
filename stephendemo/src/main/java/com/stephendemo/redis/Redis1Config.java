package com.stephendemo.redis;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jmfen
 * date 2019-11-01
 */

@Component
@Configuration
@EnableCaching
public class Redis1Config{
    //最大活跃数
    @Value("${spring.redis.jedis.pool.max-active:8}")
    private int maxActive;

    //最大等待数
    @Value("${spring.redis.jedis.pool.max-wait:-1}")
    private int maxWait;

    //最大核心线程数
    @Value("${spring.redis.jedis.pool.max-idle:8}")
    private int maxIdle;

    //最小核心线程数
    @Value("${spring.redis.jedis.pool.min-idle:0}")
    private int minIdle;

    //redis连接的超时时长
    @Value("${spring.redis.timeout:5}")
    private int timeOut;

    //redis连接的库
    @Value("${spring.redis.database:0}")
    private int database;
    //节点配置
    @Value("${spring.redis.cluster.nodes:#{null}}")
    private String nodes;

    //最大连接转移数
    @Value("${spring.redis.cluster.max-redirects:3}")
    private int maxRedirects;

    //单节点情况下redis的ip
    @Value("${spring.redis.host:#{null}}")
    private String host;

    //单节点情况下redis的端口
    @Value("${spring.redis.port:#{null}}")
    private Integer port;

    //redis的连接密码
    @Value("${spring.redis.password:#{null}}")
    private String password;

    @Bean(name = "redisTemplate1")
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("redisConnectionFactory1") RedisConnectionFactory connectionFactory){
        RedisSerializer redisSerializer = fastJson2JsonRedisSerializer();
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(redisSerializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(redisSerializer);
        return template;
    }

    @Bean
    public RedisSerializer fastJson2JsonRedisSerializer() {
        FastJsonRedisSerializer<Serializable> fast = new FastJsonRedisSerializer<Serializable>(Serializable.class);
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        fastJsonConfig.setCharset(Charset.forName("UTF-8"));
        JSONObject.DEFFAULT_DATE_FORMAT="yyyy-MM-dd HH:mm:ss";//设置日期格式
        fast.setFastJsonConfig(fastJsonConfig);
        return fast;
    }

    /**
     * 连接配置
     * @return
     */
    @Bean(name = "redisConnectionFactory1")
    public RedisConnectionFactory connectionFactory() {
        Map<String, Object> source = new HashMap<>();
        RedisClusterConfiguration redisClusterConfiguration;
        RedisStandaloneConfiguration redisStandaloneConfiguration;
        //连接池配置
        GenericObjectPoolConfig genericObjectPoolConfig =
                new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxTotal(maxActive);
        genericObjectPoolConfig.setMaxWaitMillis(maxWait);
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMinIdle(minIdle);

        //redis客户端配置
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder
                builder =  LettucePoolingClientConfiguration.builder().
                commandTimeout(Duration.ofSeconds(timeOut));
        builder.poolConfig(genericObjectPoolConfig);
        LettuceClientConfiguration lettuceClientConfiguration = builder.build();

        //集群模式
        if(nodes !=null){
            source.put("spring.redis.cluster.nodes", nodes);
            source.put("spring.redis.cluster.max-redirects", maxRedirects);
            redisClusterConfiguration = new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration", source));
            if(!StringUtils.isEmpty(password)){
                redisClusterConfiguration.setPassword(RedisPassword.of(password));
            }
            //根据配置和客户端配置创建连接
            return new LettuceConnectionFactory(redisClusterConfiguration,lettuceClientConfiguration);
        }else{
            //单机模式
            redisStandaloneConfiguration = new RedisStandaloneConfiguration(host,port);
            redisStandaloneConfiguration.setDatabase(database);
            if(!StringUtils.isEmpty(password)){
                redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
            }
            //根据配置和客户端配置创建连接工厂
            return new LettuceConnectionFactory(redisStandaloneConfiguration,lettuceClientConfiguration);

        }
    }
}