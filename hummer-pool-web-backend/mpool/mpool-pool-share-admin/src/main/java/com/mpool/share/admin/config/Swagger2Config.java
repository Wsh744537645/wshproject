package com.mpool.share.admin.config;

import com.google.common.base.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;

/**
 * swagger 配置
 * 
 * @author chen9
 *
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

	private static final Logger log = LoggerFactory.getLogger(Swagger2Config.class);

	@Bean
	public Docket createRestApi(ServletContext servletContext) {
		Predicate<RequestHandler> basePackage = RequestHandlerSelectors.basePackage("com.mpool");
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(basePackage)
				.paths(PathSelectors.any()).build();
	}

	private ApiInfo apiInfo() {
		String description = "1";
		return new ApiInfoBuilder().description(description).version("1.0.0").build();
	}
}
