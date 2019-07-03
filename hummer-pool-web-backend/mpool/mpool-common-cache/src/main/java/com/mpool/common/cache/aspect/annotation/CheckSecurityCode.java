package com.mpool.common.cache.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

/**
 * @author chen
 *检测手机验证码
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiImplicitParams(value = { 
		@ApiImplicitParam(name = "securityCode", value = "securityCode", paramType = "query") })
public @interface CheckSecurityCode {
	public String prefix() default "";
}
