package com.mpool.account.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mpool.account.mapper.UserIpAddessMapper;
import com.mpool.common.model.account.log.UserLoginLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;

public class RequestResolveUtil {

	private static final Logger log = LoggerFactory.getLogger(RequestResolveUtil.class);

	public static UserLoginLog resolveUserLoninLog(String ipAddr, Long userId, String userAgent) {
		UserLoginLog log = new UserLoginLog();
		log.setUserId(userId);
		log.setBrowser(getBrowserInfo(userAgent));
		log.setOs(getOsInfo(userAgent));
		log.setUserAgent(userAgent);
		log.setLoginIp(ipAddr);
		log.setLoginTime(new Date());
		return log;
	}

	//通过ip过去地理位置
//	private static String getCityInfoByIp(String ip){
////		ip = "183.14.132.72";
//		if (ip.equals("0:0:0:0:0:0:0:1")){
//			return null;
//		}
//		RestTemplate restTemplate = new RestTemplate();
//		try {
//		ResponseEntity<String> forEntity = restTemplate
//				.getForEntity("http://freeapi.ipip.net/{ip}", String.class,ip);
//		String fromJson =forEntity.getBody();
//
//		String countries = fromJson.substring(2,4);
//		String province = fromJson.substring(7,9);
//		String city = fromJson.substring(12,14);
//		String name = countries+"|"+province+"|"+city;
//		return name;
//		} catch (Exception e) {
//			log.error("", e);
//		}
//		return null;
//	}

	/**
	 * 获取浏览器信息
	 * 
	 * @param request
	 * @return
	 */
	public static String getBrowserInfo(String userAgent) {
		String browser = "";
		if (userAgent == null) {
			return null;
		}
		String user = userAgent.toLowerCase();
		if (user.contains("edge")) {
			browser = (userAgent.substring(userAgent.indexOf("Edge")).split(" ")[0]).replace("/", "-");
		} else if (user.contains("msie")) {
			String substring = userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
			browser = substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
		} else if (user.contains("safari") && user.contains("version")) {
			browser = (userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0] + "-"
					+ (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
		} else if (user.contains("opr") || user.contains("opera")) {
			if (user.contains("opera")) {
				browser = (userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0] + "-"
						+ (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
			} else if (user.contains("opr")) {
				browser = ((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-"))
						.replace("OPR", "Opera");
			}

		} else if (user.contains("chrome")) {
			browser = (userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
		} else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)
				|| (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1)
				|| (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1)) {
			browser = "Netscape-?";

		} else if (user.contains("firefox")) {
			browser = (userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
		} else if (user.contains("rv")) {
			String IEVersion = (userAgent.substring(userAgent.indexOf("rv")).split(" ")[0]).replace("rv:", "-");
			browser = "IE" + IEVersion.substring(0, IEVersion.length() - 1);
		} else {
			browser = "UnKnown";
		}
		return browser;
	}

	/**
	 * 获取IP
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		Enumeration<String> headerNames = request.getHeaderNames();
		if (ip == null) {

			while (headerNames.hasMoreElements()) {
				String header = headerNames.nextElement();
				String value = request.getHeader(header);
				log.debug("Header => {}:{}", header, value);
				if ("x-forwarded-for".equalsIgnoreCase(header)) {
					ip = value;
				}
			}
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if (ip.equals("127.0.0.1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (Exception e) {
					e.printStackTrace();
				}
				ip = inet.getHostAddress();
			}
		}
		// 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ip != null && ip.length() > 15) {
			if (ip.indexOf(",") > 0) {
				ip = ip.substring(0, ip.indexOf(","));
			}
		}
		return ip;
	}

	/**
	 * 获取操作系统版本信息
	 * 
	 * @param request
	 * @return
	 */
	public static String getOsInfo(String userAgent) {
		String os = "";
		// =================OS Info=======================
		if (userAgent.toLowerCase().indexOf("windows") >= 0) {
			os = "Windows";
		} else if (userAgent.toLowerCase().indexOf("mac") >= 0) {
			os = "Mac";
		} else if (userAgent.toLowerCase().indexOf("android") >= 0) {
			os = "Android";
		} else if (userAgent.toLowerCase().indexOf("iphone") >= 0) {
			os = "IPhone";
		} else if (userAgent.toLowerCase().indexOf("linux") >= 0) {
			os = "Linux";
		} else if (userAgent.toLowerCase().indexOf("x11") >= 0) {
			os = "Unix";
		} else {
			os = "UnKnown";
		}

		return os;
	}

	/**
	 * 获得本次请求的 locale
	 * 
	 * @return
	 */
	public static Locale getRequestLocale() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		Locale locale = RequestContextUtils.getLocale(request);
		return locale;
	}
}
