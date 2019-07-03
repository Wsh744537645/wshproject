package com.mpool.share.config.interceptor;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: stephen
 * @Date: 2019/5/28 11:20
 */

@SuppressWarnings("deprecation")
@SpringBootConfiguration
public class SpringMVCConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new FilterConfig());
    }
}
