package com.mpool.accountmultiple.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsClient {

	private AliyunSmsConfig aliyunSmsConfig;

	private IAcsClient client;

	public SmsClient(AliyunSmsConfig aliyunSmsConfig) throws ClientException {
		super();
		this.aliyunSmsConfig = aliyunSmsConfig;
		this.client = acsClient();
	}

	public IAcsClient getInstance() {
		return client;
	}

	public AliyunSmsConfig getAliyunSmsConfig() {
		return aliyunSmsConfig;
	}

	private IAcsClient acsClient() throws ClientException {
		// 可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", aliyunSmsConfig.getDefaultConnectTimeout() + "");
		System.setProperty("sun.net.client.defaultReadTimeout", aliyunSmsConfig.getDefaultReadTimeout() + "");

		// 初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliyunSmsConfig.getAccessKeyId(),
				aliyunSmsConfig.getAccessKeySecret());
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", aliyunSmsConfig.getProduct(),
				aliyunSmsConfig.getDomain());
		IAcsClient acsClient = new DefaultAcsClient(profile);
		return acsClient;
	}
}
