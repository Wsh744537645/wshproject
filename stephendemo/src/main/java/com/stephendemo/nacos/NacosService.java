package com.stephendemo.nacos;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author jmfen
 * date 2020-04-02
 */

@Component
@Data
@RefreshScope
public class NacosService {

    @Value("${service.name}")
    private String serverName;
}