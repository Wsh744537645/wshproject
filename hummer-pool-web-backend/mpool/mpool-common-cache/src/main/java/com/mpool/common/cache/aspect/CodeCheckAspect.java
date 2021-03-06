package com.mpool.common.cache.aspect;

import java.lang.reflect.Method;
import java.util.Arrays;

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

import com.mpool.common.cache.aspect.annotation.CheckCodeSms;
import com.mpool.common.cache.service.EmailCodeCacheService;
import com.mpool.common.cache.service.PhoneCodeCahceService;
import com.mpool.common.exception.MpoolException;

@Aspect
@Component
public class CodeCheckAspect extends BaseAspect {

	private static final Logger log = LoggerFactory.getLogger(CodeCheckAspect.class);

	@Pointcut("@annotation(com.mpool.common.cache.aspect.annotation.CheckSecurityCode)")
	public void pointcutName() {
	}

	private final static String CODE_PARAM = "securityCode";

	@Autowired
	private PhoneCodeCahceService phoneCodeCahceService;
	@Autowired
	private EmailCodeCacheService emailCodeCacheService;

	@Around("pointcutName()")
	public Object object(ProceedingJoinPoint joinPoint) throws Throwable {
		log.debug("check security code ");
		// 获取访问目标方法
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method targetMethod = methodSignature.getMethod();
		// 注解前缀
		String prefix = prefixParse(targetMethod);
		String sessionId = SecurityUtils.getSubject().getSession().getId().toString();
		// 获取 缓存中的 code
		if (prefix == null) {
			prefix = "";
		}
		String key = prefix + phoneCodeCahceService.builderKey(sessionId, getRequestCode());
		log.debug("cache security key --> {}", key);
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
			throw new MpoolException("common.phone.code.notfound", "请输入验证码");
		}
		return code;
	}

	private String getCacheCode(String key) {
		String code = phoneCodeCahceService.getCode(key);
		if (code == null) {
			code = emailCodeCacheService.getCode(key);
		}
		if (code == null) {
			throw new MpoolException("common.code.ex", "验证码错误!");
		}
		return code;
	}
}
