package com.mpool.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 币种配置信息，配置的值需与account_currency表对应
 */

@Component
@ConfigurationProperties(prefix = "currency")
@Data
public class MultipleProperties {
    private String name;

    private int id;
}
