package com.mpool.share.config.shiro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpool.common.Result;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.PrintWriter;

public class ShiroFormAuthenticationFilter extends FormAuthenticationFilter {
	ObjectMapper mapper = new ObjectMapper();

	private static final Logger log = LoggerFactory.getLogger(ShiroFormAuthenticationFilter.class);

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		// return super.onAccessDenied(request, response);
		log.debug("request url => {}",request);
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
			out.print(mapper.writeValueAsString(Result.err401()));
			out.flush();
			out.close();
//			} else {
//				this.saveRequestAndRedirectToLogin(request, response);
			// }
			return false;
		}
	}



}
