package com.mpool.share.config;

import com.mpool.common.QQCaptchaService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@ConfigurationProperties(prefix = "qq.captcha")
@Configuration
public class QQCaptchaConfig {
	private String aid;
	private String appSecretKey;
	private String verifyUrl;

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getAppSecretKey() {
		return appSecretKey;
	}

	public void setAppSecretKey(String appSecretKey) {
		this.appSecretKey = appSecretKey;
	}

	public String getVerifyUrl() {
		return verifyUrl;
	}

	public void setVerifyUrl(String verifyUrl) {
		this.verifyUrl = verifyUrl;
	}

	@Bean
	public QQCaptchaService qqCaptchaService(RestTemplate restTemplate) {
		return new QQCaptchaService(restTemplate, aid, appSecretKey, verifyUrl);
	}
}
