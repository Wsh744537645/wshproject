package com.mpool.share.service;

import com.mpool.share.model.NotifyModel;

public interface SmsService {
	/**
	 * 默认模板发送短信
	 * 
	 * @param phoneNumber
	 * @param outId
	 */
	void sendSmsCode(String phoneNumber, String code, String outId, String templateCode);

	void sendSms(String phoneNumber, String templateId, String templateParam, String outId);

	void sendAlarmSms(String phoneNumber, String templateId, NotifyModel notifyModel);
}
