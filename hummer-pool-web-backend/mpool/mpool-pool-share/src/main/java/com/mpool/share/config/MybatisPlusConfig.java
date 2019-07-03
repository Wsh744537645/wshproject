package com.mpool.share.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class MybatisPlusConfig {
	/**
	 * 事物管理器
	 * 
	 * @param dataSource
	 * @return
	 */
	@Bean(name = "shardTransactionManager")
	public DataSourceTransactionManager shardTransactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	/**
	 * 分页插件
	 * 
	 * @return
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor interceptor = new PaginationInterceptor();
		return interceptor;
	}

	/**
	 * 性能分析插件
	 * 
	 * @return
	 */
	@Bean
	public PerformanceInterceptor performanceInterceptor() {
		PerformanceInterceptor interceptor = new PerformanceInterceptor();
		return interceptor;
	}
}