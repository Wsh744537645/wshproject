package com.mpool.common.cache.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

/**
 * @author chen 
 * 检测验证码
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@CheckCode
@ApiImplicitParams(value = { @ApiImplicitParam(name = "captchaCode", value = "验证码图片", paramType = "query") })
public @interface CheckCodeCaptcha {
	@AliasFor(annotation = CheckCode.class, attribute = "prefix")
	public String prefix() default "";
}
