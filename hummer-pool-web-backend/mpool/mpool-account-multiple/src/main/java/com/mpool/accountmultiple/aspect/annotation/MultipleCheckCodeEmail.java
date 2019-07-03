package com.mpool.accountmultiple.aspect.annotation;

import com.mpool.common.cache.aspect.annotation.CheckCode;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author stephen
 *检测邮箱验证码
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@CheckCode
@ApiImplicitParams(value = {
        @ApiImplicitParam(name = "email", value = "email", paramType = "query"),
        @ApiImplicitParam(name = "emailCode", value = "emailCode", paramType = "query") })
public @interface MultipleCheckCodeEmail {
    @AliasFor(annotation = CheckCode.class, attribute = "prefix")
    public String prefix() default "";
}
