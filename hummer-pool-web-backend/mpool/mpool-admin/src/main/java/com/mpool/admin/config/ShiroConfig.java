/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.mpool.admin.config;

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

import com.mpool.admin.config.shiro.ShiroFormAuthenticationFilter;
import com.mpool.admin.config.shiro.cache.RedisShiroSessionDAO;
import com.mpool.admin.config.shiro.realm.TelphoneRealm;
import com.mpool.admin.config.shiro.realm.UsernamePasswordRealm;
import com.mpool.common.RequestResolveUtil;

/**
 * Shiro的配置文件
 *
 */
@Configuration
public class ShiroConfig {

	private static final Logger log = LoggerFactory.getLogger(ShiroConfig.class);

	@Bean("sessionManager")
	public SessionManager sessionManager(@Autowired(required = false) RedisShiroSessionDAO redisShiroSessionDAO) {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		// 设置session过期时间为1小时(单位：毫秒)，默认为30分钟
				sessionManager.setGlobalSessionTimeout(1800000);
				sessionManager.setSessionValidationSchedulerEnabled(true);
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
			SessionManager sessionManager, ModularRealmAuthenticator modularRealmAuthenticator) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setAuthenticator(modularRealmAuthenticator);
		List<Realm> realms = new ArrayList<Realm>();
		realms.add(userRealm);
		realms.add(telphoneRealm);
		log.debug("realms -> {}", realms);
		securityManager.setRealms(realms);
		securityManager.setSessionManager(sessionManager);
		return securityManager;
	}

	@Bean("shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
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

		filterMap.put("/wsh/**", "anon");
		filterMap.put("/apis/user/**", "anon");
		filterMap.put("/favicon.ico", "anon");
		filterMap.put("/views/**", "anon");
		filterMap.put("/js/**", "anon");
		filterMap.put("/img/**", "anon");
		filterMap.put("/apis/auth/**", "anon");
		filterMap.put("/auth/**", "anon");
		filterMap.put("/**", "authc");

		shiroFilter.setFilterChainDefinitionMap(filterMap);

		shiroFilter.setLoginUrl("/auth/login");

		shiroFilter.setUnauthorizedUrl("/");
		return shiroFilter;
	}

	@Bean("lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

//	@Bean
//	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
//		DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
//		proxyCreator.setProxyTargetClass(true);
//		return proxyCreator;
//	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager);
		return advisor;
	}
}
