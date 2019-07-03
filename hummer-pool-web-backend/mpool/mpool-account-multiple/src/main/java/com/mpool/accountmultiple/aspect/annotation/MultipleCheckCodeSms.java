package com.mpool.accountmultiple.aspect.annotation;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author stephen
 *检测手机验证码
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiImplicitParams(value = {
        @ApiImplicitParam(name = "phone", value = "phone", paramType = "query"),
        @ApiImplicitParam(name = "phoneCode", value = "phoneCode", paramType = "query") })
public @interface MultipleCheckCodeSms {
    String prefix() default "";
}
