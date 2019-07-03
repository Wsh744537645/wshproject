package com.mpool.admin.module.common.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.mpool.admin.config.SmsClient;
import com.mpool.admin.module.alarm.model.NotifyModel;
import com.mpool.admin.module.common.SmsService;
import com.mpool.admin.module.log.mapper.SmsLogMapper;
import com.mpool.common.model.account.log.SmsLog;

@Service
public class SmsServiceImpl implements SmsService {

	private static final Logger log = LoggerFactory.getLogger(SmsServiceImpl.class);

	@Autowired
	private SmsClient client;
	@Autowired
	private SmsLogMapper smsLogMapper;

	@Override
	public void sendAlarmSms(String phoneNumber, String templateId, String param) {

		SmsLog smsLog = new SmsLog();
		smsLog.setPhoneNumber(phoneNumber);
		smsLog.setTemplateParam(param);
		smsLog.setTemplateCode(templateId);
		smsLog.setAccessKeyId(client.getAliyunSmsConfig().getAccessKeyId());
		smsLog.setCreateTime(new Date());
		smsLog.setDomain(client.getAliyunSmsConfig().getDomain());
		// 组装请求对象-具体描述见控制台-文档部分内容
		final SendSmsRequest request = new SendSmsRequest();
		// 必填:待发送手机号
		request.setPhoneNumbers(phoneNumber);
		// 必填:短信签名-可在短信控制台中找到
		String signName = client.getAliyunSmsConfig().getSignName();
		request.setSignName(signName);
		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode(templateId);
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		request.setTemplateParam(param);
		// 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
		// request.setSmsUpExtendCode("90997");
		log.debug("send sms info templateId={},phoneNumber={},signName={}", templateId, phoneNumber, signName);
		log.debug("request => {}", request);
		try {
			SendSmsResponse response = client.getInstance().getAcsResponse(request);
			log.info("send sms success => {}", response);
			smsLog.setRequestId(response.getRequestId());
			smsLog.setBizId(response.getBizId());
			smsLog.setMessage(response.getMessage());
			smsLog.setCode(response.getCode());
		} catch (ClientException e) {
			log.error("send sms error ", e);
			smsLog.setRequestId(e.getRequestId());
			smsLog.setMessage(e.getMessage());
			smsLog.setCode(e.getErrCode());
		} finally {
			smsLogMapper.insert(smsLog);
		}
	}

}
