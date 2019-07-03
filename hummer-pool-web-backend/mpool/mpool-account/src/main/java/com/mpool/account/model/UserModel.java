package com.mpool.account.model;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import io.swagger.annotations.ApiModel;

@ApiModel
public class UserModel {
	/**
	 * 用户邮箱
	 */
	private String email;

	/**
	 * 用户手机号码
	 */
	private String phone;

	/**
	 * 用户昵称
	 */
	private String nickName;

	/**
	 * 最后一次登录IP
	 */
	private String lastIp;
	/**
	 * 最后一次登录时间
	 */
	private Date lastTime;
	/**
	 * 性别
	 */
	private String sex;
	/**
	 * 头像
	 */
	private String photo;

	/**
	 * 主账号ID
	 */
	private Long masterUserId;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getLastIp() {
		return lastIp;
	}

	public void setLastIp(String lastIp) {
		this.lastIp = lastIp;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Long getMasterUserId() {
		return masterUserId;
	}

	public void setMasterUserId(Long masterUserId) {
		this.masterUserId = masterUserId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserModel [email=").append(email).append(", phone=").append(phone).append(", nickName=")
				.append(nickName).append(", lastIp=").append(lastIp).append(", lastTime=").append(lastTime)
				.append(", sex=").append(sex).append(", photo=").append(photo).append("]");
		return builder.toString();
	}

}
