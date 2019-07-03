package com.mpool.account.service;

import com.mpool.account.model.NotifyModel;

public interface SmsService {
	/**
	 * 默认模板发送短信
	 * 
	 * @param phoneNumber
	 * @param templateParam
	 * @param outId
	 */
	void sendSmsCode(String phoneNumber, String code, String outId);

	void sendSms(String phoneNumber, String templateId, String templateParam, String outId);

	void sendAlarmSms(String phoneNumber, String templateId, NotifyModel notifyModel);
}
