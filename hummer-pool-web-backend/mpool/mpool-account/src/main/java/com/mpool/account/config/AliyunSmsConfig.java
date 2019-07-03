package com.mpool.account.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "aliyun.sms")
@Configuration
public class AliyunSmsConfig {
	// 产品名称:云通信短信API产品,开发者无需替换
	private String product = "Dysmsapi";
	// 产品域名,开发者无需替换
	private String domain = "dysmsapi.aliyuncs.com";

	private String accessKeyId;

	private String accessKeySecret;

	private long defaultConnectTimeout = 10000;

	private long defaultReadTimeout = 1000;

	private String signName;

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public long getDefaultConnectTimeout() {
		return defaultConnectTimeout;
	}

	public void setDefaultConnectTimeout(long defaultConnectTimeout) {
		this.defaultConnectTimeout = defaultConnectTimeout;
	}

	public long getDefaultReadTimeout() {
		return defaultReadTimeout;
	}

	public void setDefaultReadTimeout(long defaultReadTimeout) {
		this.defaultReadTimeout = defaultReadTimeout;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

}
