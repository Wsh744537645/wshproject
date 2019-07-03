package com.mpool.share.admin.config.spring;

import com.mpool.share.admin.exception.AdminException;
import com.mpool.common.annotation.CurrentUser;
import com.mpool.common.model.admin.SysUser;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class SysUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

	private static final Logger log = LoggerFactory.getLogger(SysUserMethodArgumentResolver.class);

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		CurrentUser user = parameter.getParameterAnnotation(CurrentUser.class);
		return user != null;
	}

	//自動注入當前用戶userid 根據註解
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		SysUser user = null;
		try {
		Object principal = SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();

		if (principal == null) {
			throw new AdminException("-1", "err");
		}
		log.debug("class type {} ", principal.getClass());
		if (principal instanceof SysUser) {
			user = (SysUser) principal;
		}
		if (user == null) {
			/*
			 * result.setCode("-1"); result.setMsg("err");
			 * result.setData("Authentication failure!");
			 */
			throw new AdminException("-1", "err");
		}
		if (log.isDebugEnabled()) {
			log.debug("current request username -> {} user -> {}", user.getUsername(), user);
		}
		}catch (Exception e){
			log.debug(e.getMessage(),e);
			throw new AdminException("-1", "err");
		}
		return user;
	}

}
