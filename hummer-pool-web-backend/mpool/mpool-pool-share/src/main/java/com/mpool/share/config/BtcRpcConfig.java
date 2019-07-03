package com.mpool.share.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: stephen
 * @Date: 2019/5/27 11:55
 */

@Configuration
@ConfigurationProperties(prefix = "rpc.btc")
@Data
public class BtcRpcConfig {
    private String user;
    private String password;
    private String allowip;
    private String port;
}
