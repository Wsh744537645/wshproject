package com.mpool.share.service.impl;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.mpool.share.config.SmsClient;
import com.mpool.share.mapper.SmsLogMapper;
import com.mpool.share.exception.ShareException;
import com.mpool.share.model.NotifyModel;
import com.mpool.share.service.SmsService;
import com.mpool.common.model.account.log.SmsLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SmsServiceImpl implements SmsService {

	private static final Logger log = LoggerFactory.getLogger(SmsServiceImpl.class);
	@Autowired
	private SmsLogMapper smsLogMapper;

	@Autowired
	private SmsClient client;

	@Override
	public void sendSmsCode(String phoneNumber, String code, String outId, String templateCode) {
		if(templateCode == null) {
			templateCode = "SMS_149419175";
		}
		// 组装请求对象-具体描述见控制台-文档部分内容
		final SendSmsRequest request = new SendSmsRequest();
		// 必填:待发送手机号
		request.setPhoneNumbers(phoneNumber);
		// 必填:短信签名-可在短信控制台中找到
		String signName = client.getAliyunSmsConfig().getSignName();
		request.setSignName(signName);
		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode(templateCode);
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		request.setTemplateParam(String.format("{\"code\":\"%s\"}", code));
		// 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
		// request.setSmsUpExtendCode("90997");

		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		request.setOutId("yourOutId");
		SmsLog entity = new SmsLog();
		entity.setDomain(client.getAliyunSmsConfig().getDomain());
		entity.setAccessKeyId(client.getAliyunSmsConfig().getAccessKeyId());
		entity.setCreateTime(new Date());
		entity.setOutId(outId);
		entity.setPhoneNumber(phoneNumber);
		entity.setTemplateCode(request.getTemplateCode());
		entity.setTemplateParam(request.getTemplateParam());

		entity.setCode(code);
		log.debug("send sms info templateCode={},phoneNumber={},signName={}", templateCode, phoneNumber, signName,
				code);
		try {
			SendSmsResponse response = client.getInstance().getAcsResponse(request);
			String bizId = response.getBizId();
			entity.setRequestId(response.getRequestId());
			entity.setBizId(bizId);
			entity.setResponseCode(response.getCode());
			entity.setMessage(response.getMessage());
			response.getRequestId();
			smsLogMapper.insert(entity);
		} catch (ClientException e) {
			entity.setResponseCode(e.getErrCode());
			entity.setMessage(e.getErrMsg());
			entity.setRequestId(e.getRequestId());
			smsLogMapper.insert(entity);
			log.error("send sms error ", e);
			throw new ShareException(e.getErrCode(), e.getErrMsg());
		}
	}


	@Override
	public void sendSms(String phoneNumber, String templateId, String templateParam, String outId) {
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
		request.setTemplateParam(templateParam);
		// 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
		// request.setSmsUpExtendCode("90997");

		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		request.setOutId("yourOutId");
		SmsLog entity = new SmsLog();
		entity.setDomain(client.getAliyunSmsConfig().getDomain());
		entity.setAccessKeyId(client.getAliyunSmsConfig().getAccessKeyId());
		entity.setCreateTime(new Date());
		entity.setOutId(outId);
		entity.setPhoneNumber(phoneNumber);
		entity.setTemplateCode(request.getTemplateCode());
		entity.setTemplateParam(request.getTemplateParam());

		log.debug("send sms info templateCode={},phoneNumber={},signName={}, templateParam = {}", templateId, phoneNumber, signName,
				templateParam);
		try {
			SendSmsResponse response = client.getInstance().getAcsResponse(request);
			String bizId = response.getBizId();
			entity.setRequestId(response.getRequestId());
			entity.setBizId(bizId);
			entity.setResponseCode(response.getCode());
			entity.setMessage(response.getMessage());
			response.getRequestId();
			smsLogMapper.insert(entity);
		} catch (ClientException e) {
			entity.setResponseCode(e.getErrCode());
			entity.setMessage(e.getErrMsg());
			entity.setRequestId(e.getRequestId());
			smsLogMapper.insert(entity);
			log.error("send sms error ", e);
			throw new ShareException(e.getErrCode(), e.getErrMsg());
		}
	}

	@Override
	public void sendAlarmSms(String phoneNumber, String templateId, NotifyModel notifyModel) {
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
		request.setTemplateParam(
				String.format("{\"name\":\"%s\", \"value\":\"%s\"}", notifyModel.getName(), notifyModel.getValue()));
		// 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
		// request.setSmsUpExtendCode("90997");

		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		SmsLog entity = new SmsLog();
		entity.setDomain(client.getAliyunSmsConfig().getDomain());
		entity.setAccessKeyId(client.getAliyunSmsConfig().getAccessKeyId());
		entity.setCreateTime(new Date());
		entity.setOutId(null);
		entity.setPhoneNumber(phoneNumber);
		entity.setTemplateCode(request.getTemplateCode());
		entity.setTemplateParam(request.getTemplateParam());

		entity.setCode(null);
		log.debug("send sms info templateId={},phoneNumber={},signName={}", templateId, phoneNumber, signName);
		log.debug("request => {}", request);
		try {
			SendSmsResponse response = client.getInstance().getAcsResponse(request);
			String bizId = response.getBizId();
			entity.setRequestId(response.getRequestId());
			entity.setBizId(bizId);
			entity.setResponseCode(response.getCode());
			entity.setMessage(response.getMessage());
			response.getRequestId();
			smsLogMapper.insert(entity);
		} catch (ClientException e) {
			entity.setResponseCode(e.getErrCode());
			entity.setMessage(e.getErrMsg());
			entity.setRequestId(e.getRequestId());
			smsLogMapper.insert(entity);
			log.error("send sms error ", e);
			throw new ShareException(e.getErrCode(), e.getErrMsg());
		}
	}

}
