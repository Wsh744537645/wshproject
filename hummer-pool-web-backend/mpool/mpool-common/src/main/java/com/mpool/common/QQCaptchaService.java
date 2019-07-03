package com.mpool.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mpool.common.exception.MpoolException;

public class QQCaptchaService {

	private static final Logger log = LoggerFactory.getLogger(QQCaptchaService.class);

	private RestTemplate restTemplate;
	private String aid;
	private String appSecretKey;
	private String verifyUrl;
	private final static String _App_Secret_Key = "AppSecretKey";
	private final static String _aid = "aid";
	private final static String _Ticket = "Ticket";
	private final static String _Randstr = "Randstr";
	private final static String _UserIP = "UserIP";
	private final static String P = "?";
	private final static String D = "=";
	private final static String Y = "&";

	private final static String resp_key_response = "response";
	private final static String resp_key_evil_level = "evil_level";
	private final static String resp_key_err_msg = "err_msg";

	private final static int success = 1;

	public QQCaptchaService(RestTemplate restTemplate, String aid, String appSecretKey, String verifyUrl) {
		super();
		this.restTemplate = restTemplate;
		this.aid = aid;
		this.appSecretKey = appSecretKey;
		this.verifyUrl = verifyUrl;
	}
	
	public void verify(String ticket, String tandstr, String userIp) {
		String url = buildRequest(ticket, tandstr, userIp);
		if (log.isDebugEnabled()) {
			log.debug("verify url ->{} ", url);
		}
		ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);
		String body = resp.getBody();
		if (log.isDebugEnabled()) {
			log.debug("verify body ->{}", body);
		}
		Gson gson = new Gson();
		JsonObject fromJson = gson.fromJson(body, JsonObject.class);
		int asInt = fromJson.get(resp_key_response).getAsInt();
		if (asInt == success) {

		} else {
			throw new MpoolException(fromJson.get(resp_key_err_msg).getAsString(),
					fromJson.get(resp_key_err_msg).getAsString());
		}
		// {response:1, evil_level:70, err_msg:""}

	}

	private String buildRequest(String ticket, String tandstr, String userIp) {
		StringBuilder sb = new StringBuilder();
		sb.append(verifyUrl).append(P).append(_aid).append(D).append(aid).append(Y).append(_App_Secret_Key).append(D)
				.append(appSecretKey).append(Y).append(_Ticket).append(D).append(ticket).append(Y).append(_Randstr)
				.append(D).append(tandstr).append(Y).append(_UserIP).append(D).append(userIp);
		return sb.toString();
	}
}
