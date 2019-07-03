package com.mpool.accountmultiple.aspect;

import com.mpool.accountmultiple.utils.AccountUtils;
import com.mpool.common.cache.aspect.BaseAspect;
import com.mpool.common.cache.aspect.annotation.CheckCodeSms;
import com.mpool.common.cache.service.PhoneCodeCahceService;
import com.mpool.common.exception.MpoolException;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class MultipleSmsCodeCheckAspect extends BaseAspect {
    private static final Logger log = LoggerFactory.getLogger(MultipleSmsCodeCheckAspect.class);

    @Pointcut("@annotation(com.mpool.accountmultiple.aspect.annotation.MultipleCheckCodeSms)")
    public void pointcutName() {
    }

    private final static String CODE_PARAM = "phoneCode";

    private final static String PHONE_PARAM = "phone";
    @Autowired
    private PhoneCodeCahceService phoneCodeCahceService;

    @Around("pointcutName()")
    public Object object(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("multiple check phone code ");
        // 获取访问目标方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        // 注解前缀
        String prefix = prefixParse(targetMethod);
        String sessionId = AccountUtils.getSessionID();
        // 获取 缓存中的 code
        String key = prefix + phoneCodeCahceService.builderKey(sessionId, getRequestCode());
        log.debug("multiple cache phone key --> {}", key);
        getCacheCode(key);
        return joinPoint.proceed();
    }

    /*
     * 获取注解信息
     */
    @Override
    public String prefixParse(Method method) throws Exception {
        if (method.isAnnotationPresent(CheckCodeSms.class)) {
            CheckCodeSms annotation = method.getAnnotation(CheckCodeSms.class);
            return annotation.prefix();
        }
        return null;
    }

    private String getRequestCode() {
        getRequest().getParameterMap().entrySet().stream().forEach(e -> {
            log.debug((e.getKey() + ":" + Arrays.toString(e.getValue())));
        });
        String code = getRequest().getParameter(CODE_PARAM);
        if (StringUtils.isEmpty(code)) {
            code = (String) getRequest().getAttribute(CODE_PARAM);
            if (StringUtils.isEmpty(code)) {
                throw new MpoolException("common.phone.code.notfound", "请输入手机短信验证码");
            }
        }
        return code;
    }

    private String getPhone() {
        String email = getRequest().getParameter(PHONE_PARAM);
        if (StringUtils.isEmpty(email)) {
            throw new MpoolException("common.phone.notfound", "请输入手机号码!");
        }
        return email;
    }

    private String getCacheCode(String key) {
        String code = phoneCodeCahceService.getCode(key);
        if (code == null) {
            throw new MpoolException("common.code.ex", "验证码错误!");
        }
        return code;
    }
}
