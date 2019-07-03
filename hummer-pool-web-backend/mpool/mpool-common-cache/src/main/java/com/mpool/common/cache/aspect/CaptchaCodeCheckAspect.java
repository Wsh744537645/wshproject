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

import com.mpool.common.cache.service.CaptchaCacheService;
import com.mpool.common.exception.MpoolException;

@Component
@Aspect
public class CaptchaCodeCheckAspect extends BaseAspect {

	private static final Logger log = LoggerFactory.getLogger(CaptchaCodeCheckAspect.class);

	@Autowired
	private CaptchaCacheService captchaCacheService;

	@Pointcut("@annotation(com.mpool.common.cache.aspect.annotation.CheckCodeCaptcha)")
	public void pointcutName() {
	}

	private final static String CODE_PARAM = "captchaCode";

	@Around("pointcutName()")
	public Object object(ProceedingJoinPoint joinPoint) throws Throwable {
		log.debug("check captcha code ");
		// 获取访问目标方法
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method targetMethod = methodSignature.getMethod();
		String prefix = prefixParse(targetMethod);
		System.out.println(prefix);
		String sessionId = SecurityUtils.getSubject().getSession().getId().toString();
		// 获取 缓存中的 code
		boolean flag = getRequestCode().equalsIgnoreCase(getCacheCode(prefix + sessionId));
		if (flag) {
			log.debug("endcheck captcha code ");
			return joinPoint.proceed();

		}
		throw new MpoolException("common.captcha.code.error", "验证码错误");
	}

	private String getRequestCode() {
		getRequest().getParameterMap().entrySet().stream().forEach(e -> {
			log.debug((e.getKey() + ":" + Arrays.toString(e.getValue())));
		});
		String code = getRequest().getParameter(CODE_PARAM);
		if (StringUtils.isEmpty(code)) {
			throw new MpoolException("common.captcha.code.notfound", "验证码没有找到");
		}
		return code;
	}

	private String getCacheCode(String key) {
		return captchaCacheService.getCode(key);
	}
}
