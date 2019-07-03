package com.mpool.account.config.shiro;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public abstract class AbstractRestAPIAuthenticationFilter extends AuthenticationFilter {
	public static final String TOKEN = "token";

	private static final Logger log = LoggerFactory.getLogger(AbstractRestAPIAuthenticationFilter.class);

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest req = (HttpServletRequest) request;
		if (req.getMethod().equals("OPTIONS")) {
			return true;
		}

		log.info("" + req.getRequestURI());
		String token = req.getHeader(TOKEN);
		if (isAccess(token)) {
			return onAccessSuccess(req, (HttpServletResponse) response);
		}
		return onAccessFail(req, (HttpServletResponse) response);
	}

	/**
	 * 判断token的合法性
	 * 
	 * @param token
	 * @return
	 */
	public abstract boolean isAccess(String token);

	/**
	 * 认证成功进行的操作处理
	 * 
	 * @param request
	 * @param response
	 * @return true 继续后续处理，false 不需要后续处理
	 */
	public abstract boolean onAccessSuccess(HttpServletRequest request, HttpServletResponse response)
			throws IOException;

	/**
	 * 认证失败时处理结果
	 * 
	 * @param request
	 * @param response
	 * @return true 继续后续处理，false 不需要后续处理
	 */
	public abstract boolean onAccessFail(HttpServletRequest request, HttpServletResponse response) throws IOException;

}
