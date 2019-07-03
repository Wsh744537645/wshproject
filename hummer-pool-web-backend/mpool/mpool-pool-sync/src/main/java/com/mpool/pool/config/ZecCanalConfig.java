package com.mpool.pool.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.mpool.zec-canal")
public class ZecCanalConfig extends CanalConfig {
}
