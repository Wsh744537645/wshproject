package com.mpool.share.admin.config.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author chenjian2
 * ip 白名单 
 * 放在nginx 哪里做
 */
@ConfigurationProperties(prefix = "system.security")
@WebFilter(value = "/**")
//@Component
public class IpWhiteFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(IpWhiteFilter.class);

	private String[] IpWhitelist;

	public void setIpWhitelist(String[] ipWhitelist) {
		IpWhitelist = ipWhitelist;
	}

	/**
	 * 匹配是否是白名单
	 * 
	 * @param ip
	 * @return
	 */
	private boolean isMatchWhiteList(String ip) {
		List<String> list = Arrays.asList(IpWhitelist);
		if (list.contains(ip)) {
			return true;
		}
		return list.stream().anyMatch(data -> ip.startsWith(data));
	}

	/**
	 * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
	 * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
	 * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
	 * 
	 * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130, 192.168.1.100
	 * 
	 * 用户真实IP为： 192.168.1.110
	 * 
	 * @param request
	 * @return
	 */
	private String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		if (IpWhitelist == null || IpWhitelist.length == 0) {
			IpWhitelist = new String[] { "0:0:0:0:0:0:0:1", "127.0.0.1" };
		}
		log.info("Ip white list filter is init.....");
		log.debug("White list -> {}", Arrays.asList(IpWhitelist));

	}

	@Override
	public void doFilter(ServletRequest srequest, ServletResponse sresponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) srequest;
		HttpServletResponse response = (HttpServletResponse) sresponse;
		String ip = this.getIpAddress(request);
		log.info("The requester IP is {}", ip);
		if (!isMatchWhiteList(ip)) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(401);
			response.getOutputStream().flush();
			response.getOutputStream().close();
			return;
		}
		chain.doFilter(srequest, sresponse);
	}

	@Override
	public void destroy() {

	}
}
