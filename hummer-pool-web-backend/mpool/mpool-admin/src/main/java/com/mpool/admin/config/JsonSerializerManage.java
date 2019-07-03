package com.mpool.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Component
public class JsonSerializerManage {
	@Bean
	@Profile("prod")
	public ObjectMapper jacksonObjectMapperProd(Jackson2ObjectMapperBuilder builder) {
		ObjectMapper objectMapper = builder.createXmlMapper(false).build();
		// 忽略value为null 时 key的输出
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		/**
		 * 序列换成json时,将所有的long变成string 因为js中得数字类型不能包含所有的java long值
		 */
		SimpleModule module = new SimpleModule();
		module.addSerializer(Long.class, ToStringSerializer.instance);
		module.addSerializer(Long.TYPE, ToStringSerializer.instance);
		objectMapper.registerModule(module);
		return objectMapper;
	}

	@Bean
	@Profile("test")
	public ObjectMapper jacksonObjectMapperTest(Jackson2ObjectMapperBuilder builder) {
		ObjectMapper objectMapper = builder.createXmlMapper(false).build();
		// 忽略value为null 时 key的输出
//		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		/**
		 * 序列换成json时,将所有的long变成string 因为js中得数字类型不能包含所有的java long值
		 */
		SimpleModule module = new SimpleModule();
		module.addSerializer(Long.class, ToStringSerializer.instance);
		module.addSerializer(Long.TYPE, ToStringSerializer.instance);
		objectMapper.registerModule(module);
		return objectMapper;
	}
}
