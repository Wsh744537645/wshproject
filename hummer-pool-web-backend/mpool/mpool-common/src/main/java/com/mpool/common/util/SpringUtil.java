package com.mpool.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringUtil implements ApplicationContextAware {

	private static final Logger log = LoggerFactory.getLogger(SpringUtil.class);

	private static String[] activeprofile;

	@Value("spring.profiles.active")
	private static ApplicationContext applicationContext;

	public static void setActiveprofile(String[] activeprofile) {
		SpringUtil.activeprofile = activeprofile;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (SpringUtil.applicationContext == null) {
			SpringUtil.applicationContext = applicationContext;
		}
	}

	// 获取applicationContext
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	// 通过name获取 Bean.
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}

	// 通过class获取Bean.
	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	// 通过name,以及Clazz返回指定的Bean
	public static <T> T getBean(String name, Class<T> clazz) {
		return getApplicationContext().getBean(name, clazz);
	}

	/**
	 * 获取当前环境
	 */
	public static String getActiveProfile() {
		String prof = applicationContext.getEnvironment().getActiveProfiles()[0];
		if (applicationContext.getEnvironment().getActiveProfiles()[0] != null) {

			prof = applicationContext.getEnvironment().getActiveProfiles()[0];
		} else {
			if (activeprofile == null || activeprofile.length == 0) {
				prof = "prod";
			} else {
				prof = activeprofile[0];
			}
		}
		log.debug("prof => {}", prof);
		return prof;
	}
}
