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

import com.mpool.common.cache.service.EmailCodeCacheService;
import com.mpool.common.exception.MpoolException;

@Aspect
@Component
public class EmailCodeCheckAspect extends BaseAspect {

	private static final Logger log = LoggerFactory.getLogger(EmailCodeCheckAspect.class);

	private final static String CODE_PARAM = "emailCode";

	private final static String EMAIL_PARAM = "email";
	@Autowired
	private EmailCodeCacheService emailCodeCacheService;

	@Pointcut("@annotation(com.mpool.common.cache.aspect.annotation.CheckCodeEmail)")
	public void pointcutName() {
	}

	@Around("pointcutName()")
	public Object object(ProceedingJoinPoint joinPoint) throws Throwable {
		log.debug("check email code ");
		// 获取访问目标方法
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method targetMethod = methodSignature.getMethod();
		String prefix = prefixParse(targetMethod);
		String sessionId = SecurityUtils.getSubject().getSession().getId().toString();
		// 获取 缓存中的 code
		String key = prefix + emailCodeCacheService.builderKey(sessionId, getRequestCode());
		log.debug("cache email key --> {}", key);
		getCacheCode(key);
		return joinPoint.proceed();
	}

	private String getRequestCode() {
		getRequest().getParameterMap().entrySet().stream().forEach(e -> {
			log.debug((e.getKey() + ":" + Arrays.toString(e.getValue())));
		});
		String code = getRequest().getParameter(CODE_PARAM);
		if (StringUtils.isEmpty(code)) {
			throw new MpoolException("common.email.code.notfound", "邮箱验证码错误");
		}
		return code;
	}

	private String getEmail() {
		String email = getRequest().getParameter(EMAIL_PARAM);
		if (StringUtils.isEmpty(email)) {
			throw new MpoolException("common.email.notfound", "请输入邮箱!");
		}
		return email;
	}

	private String getCacheCode(String key) {
		String code = emailCodeCacheService.getCode(key);
		if (code == null) {
			throw new MpoolException("common.code.notfound", "验证码错误!");
		}
		return code;
	}
}
