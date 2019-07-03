package com.mpool.share.config;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

public class MpoolWebSessionManager extends DefaultWebSessionManager {

	private static final Logger log = LoggerFactory.getLogger(MpoolWebSessionManager.class);

	/**
	 * 这个是客户端请求给服务端带的header
	 */
	public final static String HEADER_TOKEN_NAME = "token";
	private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";

	@Override
	protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
		String id = WebUtils.toHttp(request).getHeader(HEADER_TOKEN_NAME);
		// System.out.println("id："+id);
		log.debug("id=>{}", id);
		if (StringUtils.isEmpty(id)) {
			// 如果没有携带id参数则按照父类的方式在cookie进行获取
			return super.getSessionId(request, response);
		} else {
			// 如果请求头中有 authToken 则其值为sessionId
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
			return id;
		}
	}

}
