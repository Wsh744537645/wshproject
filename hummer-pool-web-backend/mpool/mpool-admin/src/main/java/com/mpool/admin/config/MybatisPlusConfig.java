package com.mpool.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;

@EnableTransactionManagement
@Configuration
public class MybatisPlusConfig {
	/**
	 * 分页插件
	 * @return
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor interceptor = new PaginationInterceptor();
		// interceptor.setDialectType("mysql")
		return interceptor;
	}

	/**
	 * 性能分析插件
	 * @return
	 */
	@Bean
	public PerformanceInterceptor performanceInterceptor() {
		PerformanceInterceptor interceptor = new PerformanceInterceptor();
		return interceptor;
	}
}