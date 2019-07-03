package com.mpool.common.model.account.log;

import static com.mpool.common.model.constant.AccountConstant.TABLE_PREFIX;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("log_sms")
public class SmsLog {
	@TableId
	private Integer id;

	/**
	 * 手机号码
	 */
	private String phoneNumber;

	/**
	 * 域名
	 */
	private String domain;

	/**
	 * 模板id
	 */
	private String templateCode;

	/**
	 * 模板参数
	 */
	private String templateParam;

	/**
	 * 发送Id
	 */
	private String outId;

	/**
	 * akey
	 */
	private String accessKeyId;

	private String requestId;

	private Date createTime;

	private String message;

	private String code;

	private String responseCode;
	private String responseOutId;
	private String bizId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getTemplateParam() {
		return templateParam;
	}

	public void setTemplateParam(String templateParam) {
		this.templateParam = templateParam;
	}

	public String getOutId() {
		return outId;
	}

	public void setOutId(String outId) {
		this.outId = outId;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseOutId() {
		return responseOutId;
	}

	public void setResponseOutId(String responseOutId) {
		this.responseOutId = responseOutId;
	}
}
