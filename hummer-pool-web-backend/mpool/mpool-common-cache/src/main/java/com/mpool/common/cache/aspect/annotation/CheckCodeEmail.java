package com.mpool.common.cache.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

/**
 * @author chen
 *检测邮箱验证码
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@CheckCode
@ApiImplicitParams(value = { 
		@ApiImplicitParam(name = "email", value = "email", paramType = "query"),
		@ApiImplicitParam(name = "emailCode", value = "emailCode", paramType = "query") })
public @interface CheckCodeEmail {
	@AliasFor(annotation = CheckCode.class, attribute = "prefix")
	public String prefix() default "";
}
