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
public class UpdateAspect {

	private static final Logger log = LoggerFactory.getLogger(UpdateAspect.class);

	@Pointcut("@annotation(com.mpool.common.model.aspect.annotation.Update)")
	public void updatePointcut() {
	}

	public UpdateAspect() {
		super();
		log.debug("----------------->init UpdateAspect");
	}

	@Before("updatePointcut()")
	public void before(JoinPoint pjp) {
		log.debug("----------------->before ");
		System.out.println(pjp.getTarget().toString());
		System.out.println("---------------->" + pjp);
	}
}
