package com.mpool.common.model.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class InsertAspect {

	private static final Logger log = LoggerFactory.getLogger(InsertAspect.class);

	// @Pointcut("execution(public com.mpool.admin.module.*.service.impl.*.*(..))")
	@Pointcut("@annotation(com.mpool.common.model.aspect.annotation.Insert)")
	public void insertPointcut() {
	}

	@Before("insertPointcut()")
	public void before1(JoinPoint pjp) {
		log.debug("----------------->before InsertAspect");
		System.out.println(pjp.getTarget().toString());
		System.out.println("---------------->" + pjp);
	}
}
