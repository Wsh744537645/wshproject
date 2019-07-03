package com.mpool.account.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.mpool.account.config.shiro.ShiroFormAuthenticationFilter;
import com.mpool.account.config.shiro.cache.RedisShiroSessionDAO;
import com.mpool.account.config.shiro.realm.TelphoneRealm;
import com.mpool.account.config.shiro.realm.UsernamePasswordRealm;
import com.mpool.account.utils.RequestResolveUtil;

/**
 * Shiro的配置文件
 *
 * @author Mark sunlightcs@gmail.com
 * @since 3.0.0 2017-09-27
 */
@Configuration
public class ShiroConfig {

	private static final Logger log = LoggerFactory.getLogger(ShiroConfig.class);

	@Bean("sessionManager")
	public SessionManager sessionManager(@Autowired(required = false) RedisShiroSessionDAO redisShiroSessionDAO) {
		// 使用自定义的session获取方式
		MpoolWebSessionManager sessionManager = new MpoolWebSessionManager();
		// 设置session过期时间为1小时(单位：毫秒)，默认为30分钟
		sessionManager.setGlobalSessionTimeout(1800000);
		sessionManager.setSessionValidationSchedulerEnabled(true);
		//定时查询所有session是否过期的时间
		sessionManager.setSessionValidationInterval(1800000);
		sessionManager.setSessionIdUrlRewritingEnabled(false);
		sessionManager.setSessionFactory(new SessionFactory() {

			@Override
			public Session createSession(SessionContext initData) {
				HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
						.getRequest();

				String host = getHost(request);
				return new SimpleSession(host);
			}

			private String getHost(HttpServletRequest request) {
				log.debug("request -> {}", request);
				String host = RequestResolveUtil.getIpAddress(request);
				log.debug("request host -> {}", host);
				return host;
			}
		});
		// 如果开启redis缓存且renren.shiro.redis=true，则shiro session存到redis里
		if (redisShiroSessionDAO != null) {
			sessionManager.setSessionDAO(redisShiroSessionDAO);
		}
		return sessionManager;
	}

	@Bean
	public ModularRealmAuthenticator modularRealmAuthenticator() {
		ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();
		modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
		return modularRealmAuthenticator;
	}

	@Bean("securityManager")
	public SecurityManager securityManager(UsernamePasswordRealm userRealm, TelphoneRealm telphoneRealm,
			SessionManager sessionManager, RedisTemplate redisTemplate,
			ModularRealmAuthenticator modularRealmAuthenticator) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// securityManager.setRealm(userRealm);
		securityManager.setAuthenticator(modularRealmAuthenticator);
		List<Realm> realms = new ArrayList<Realm>();
		// userRealm.setCredentialsMatcher(new CustomCredentialsMatcher(new
		// PasswordRetryCache(redisTemplate)));
		realms.add(userRealm);
		realms.add(telphoneRealm);
		log.debug("realms -> {}", realms);
		securityManager.setRealms(realms);

		securityManager.setSessionManager(sessionManager);
		return securityManager;
	}

	@Bean("shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
		MpoolShiroFilterFactoryBean shiroFilter = new MpoolShiroFilterFactoryBean();
		shiroFilter.setSecurityManager(securityManager);
		Map<String, Filter> filters = new HashMap<>();
		filters.put("authc", new ShiroFormAuthenticationFilter());
		shiroFilter.setFilters(filters);
		Map<String, String> filterMap = new LinkedHashMap<>();
		filterMap.put("/swagger/**", "anon");
		filterMap.put("/", "anon");
		filterMap.put("/v2/api-docs", "anon");
		filterMap.put("/swagger-ui.html", "anon");
		filterMap.put("/webjars/**", "anon");
		filterMap.put("/swagger-resources/**", "anon");
		filterMap.put("/index.html", "anon");
		filterMap.put("/app/**", "anon");
		filterMap.put("/static/**", "anon");
		filterMap.put("/test/**", "anon");
		filterMap.put("/favicon.ico", "anon");
		filterMap.put("/views/**", "anon");
		filterMap.put("/js/**", "anon");
		filterMap.put("/img/**", "anon");
		filterMap.put("/css/**", "anon");
		// 分享页面
		filterMap.put("/share/**", "anon");
		filterMap.put("/apis/share/**", "anon");

		filterMap.put("/wx/**", "anon");
		filterMap.put("/apis/wx/**", "anon");

		filterMap.put("/pool/**", "anon");
		filterMap.put("/apis/pool/**", "anon");

		filterMap.put("/msg/**", "anon");
		filterMap.put("/apis/msg/**", "anon");

		filterMap.put("/miner/**", "anon");
		filterMap.put("/apis/miner/**", "anon");

		filterMap.put("/user/menus", "authc");
		filterMap.put("/apis/user/menus", "authc");

		//filterMap.put("/apis/user/dashbaord/**", "authc");
		//filterMap.put("/user/dashbaord/**", "authc");

		filterMap.put("/apis/wx/**", "anon");
		filterMap.put("/user/**", "anon");
		filterMap.put("/apis/user/**", "anon");
		//filterMap.put("/**", "authc");

		shiroFilter.setFilterChainDefinitionMap(filterMap);

		shiroFilter.setLoginUrl("/user/login");

		shiroFilter.setUnauthorizedUrl("/");

		return shiroFilter;
	}

	@Bean("lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
		proxyCreator.setProxyTargetClass(true);
		return proxyCreator;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager);
		return advisor;
	}
}
