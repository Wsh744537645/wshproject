package com.mpool.admin.config.shiro;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpool.common.Result;

public class ShiroFormAuthenticationFilter extends FormAuthenticationFilter {
	ObjectMapper mapper = new ObjectMapper();

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		// return super.onAccessDenied(request, response);
		if (this.isLoginRequest(request, response)) {
			if (this.isLoginSubmission(request, response)) {
				return this.executeLogin(request, response);
			} else {
				return true;
			}
		} else {
			// String header = ((HttpServletRequest) request).getHeader("Content-Type");
			// if (header != null && header.equals("application/x-www-form-urlencoded"))// {
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.print(mapper.writeValueAsString(Result.err()));
			out.flush();
			out.close();
//			} else {
//				this.saveRequestAndRedirectToLogin(request, response);
			// }
			return false;
		}
	}
}
