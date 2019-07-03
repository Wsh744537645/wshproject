package com.mpool.share.config.spring;

import com.mpool.share.utils.AccountSecurityUtils;
import com.mpool.common.annotation.CurrentUser;
import com.mpool.share.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UserMethodArgumentResolver implements HandlerMethodArgumentResolver {

	private static final Logger log = LoggerFactory.getLogger(UserMethodArgumentResolver.class);

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		CurrentUser user = parameter.getParameterAnnotation(CurrentUser.class);
		return user != null;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		User user = AccountSecurityUtils.getUser();
		if (log.isDebugEnabled()) {
			log.debug("current request username -> {} user -> {}", user.getUsername(), user);
		}
		return user;
	}

}
