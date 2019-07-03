package com.mpool.account.config.shiro;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;

import com.mpool.common.util.JWTUtil;

@Deprecated
public class RestAPIAuthFilter extends AbstractRestAPIAuthenticationFilter {

	@Override
	public boolean isAccess(String token) {
		// TODO Auto-generated method stub
		return JWTUtil.verify(token);
	}

	@Override
	public boolean onAccessSuccess(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Subject subject = getSubject(request, response);
		boolean permitted = false;
		do {
			if (subject == null) {
				permitted = false;
				continue;
			}
			permitted = true;
			// subject.isPermitted(new SysPermis(
			// request.getRequestURI(), request.getMethod()));
		} while (false);

		if (!permitted) {
			response.setHeader("Content-Type", "application/json;charset=UTF-8");
			WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "权限不足！");
		}
		return permitted;
	}

	@Override
	public boolean onAccessFail(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		WebUtils.toHttp(response).sendError(HttpServletResponse.SC_FORBIDDEN, "请重新登录！");
		return false;
	}
}
