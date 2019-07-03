package com.mpool.common.model.account.log;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("log_email")
public class EmailLog {
	/**
	 * 主键
	 */
	@TableId
	private Integer emailId;

	/**
	 * 发送目标ID
	 */
	private String userId;

	/**
	 * 邮箱内容
	 */
	private String emaliContent;

	/**
	 * 发送的 邮箱号
	 */
	private String email;

	/**
	 * 邮件服务地址
	 */
	private String domain;

	/**
	 * 目的地邮箱号
	 */
	private String destinationEmail;
	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 邮件中的验证码
	 */
	private String emailCode;

	public Integer getEmailId() {
		return emailId;
	}

	public void setEmailId(Integer emailId) {
		this.emailId = emailId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmaliContent() {
		return emaliContent;
	}

	public void setEmaliContent(String emaliContent) {
		this.emaliContent = emaliContent;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getDestinationEmail() {
		return destinationEmail;
	}

	public void setDestinationEmail(String destinationEmail) {
		this.destinationEmail = destinationEmail;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getEmailCode() {
		return emailCode;
	}

	public void setEmailCode(String emailCode) {
		this.emailCode = emailCode;
	}

}
