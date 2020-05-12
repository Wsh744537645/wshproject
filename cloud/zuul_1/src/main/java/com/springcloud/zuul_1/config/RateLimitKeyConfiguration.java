package com.springcloud.zuul_1.config;

import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.RateLimitKeyGenerator;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.RateLimitUtils;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.properties.RateLimitProperties;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.DefaultRateLimitKeyGenerator;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;

/**
 * rateLimit 自定义Key策略
 * @author jmfen
 * date 2020-05-12
 */

@Configuration
public class RateLimitKeyConfiguration {

    @Bean
    public RateLimitKeyGenerator rateLimitKeyGenerator(final RateLimitProperties rateLimitProperties, final RateLimitUtils rateLimitUtils){
        return new DefaultRateLimitKeyGenerator(rateLimitProperties, rateLimitUtils){

            @Override
            public String key(HttpServletRequest request, Route route, RateLimitProperties.Policy policy){
                System.out.println("==================" + request.getParameter("name"));
                return super.key(request, route, policy) + ":" + request.getParameter("name");
            }
        };
    }
}