package com.mpool.common.cache.aspect;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.mpool.common.cache.aspect.annotation.CheckCode;

public class BaseAspect {
	public HttpServletRequest getRequest() {
		ServletRequestAttributes ra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = ra.getRequest();
		ra.getResponse();
		return request;
	}

	public HttpServletResponse getResponse() {
		ServletRequestAttributes ra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletResponse response = ra.getResponse();
		return response;
	}

	public String prefixParse(Method method) throws Exception {
		CheckCode findMergedAnnotation = AnnotatedElementUtils.findMergedAnnotation(method, CheckCode.class);
		return findMergedAnnotation.prefix();
	}
}
