package com.mpool.common.model.account;

import static com.mpool.common.model.constant.AccountConstant.TABLE_PREFIX;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(TABLE_PREFIX + "user")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9020204956664763485L;

	/**
	 * 用户Id
	 */
	@TableId
	private Long userId;

	/**
	 * 登录名字
	 */
	private String username;

	/**
	 * 安全码
	 */
	private String loginSecret;

	/**
	 * 用户密码
	 */
	private String password;

	/**
	 * 主账号用户ID
	 */
	private Long masterUserId;

	/**
	 * 用户类型 master=主账号 member=子账号
	 */
	private String userType;

	/**
	 * 用户手机
	 */
	private String userPhone;

	/**
	 * 用户邮箱
	 */
	private String userEmail;

	/**
	 * 用户名称
	 */
	private String nickName;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 创建人
	 */
	private Long createBy;
	/**
	 * 最后一次登录IP
	 */
	private String lastIp;

	/**
	 * 最后一次登录时间
	 */
	private Date lastTime;

	/**
	 * 用户来源
	 */
	private String userFrom;

	/**
	 * 用户性别
	 */
	private String userSex;
	/**
	 * 用户头像
	 */
	private String userPhoto;

	/**
	 * 用户的语言类型
	 */
	private String langType;

	/**
	 * 微信唯一标示openid
	 */
	private String openid;
	/**
	 * 会员类型0普通会员 1推荐人 基石
	 */
	private Integer userGroup;
	/**
	 * 推荐人 不为数据库字段false
	 */
	@TableField(exist = false)
	private String recommendName;

	public String getRecommendName() {
		return recommendName;
	}

	public void setRecommendName(String recommendName) {
		this.recommendName = recommendName;
	}

	public Integer getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(Integer userGroup) {
		this.userGroup = userGroup;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getMasterUserId() {
		return masterUserId;
	}

	public void setMasterUserId(Long masterUserId) {
		this.masterUserId = masterUserId;
	}

	public String getLangType() {
		return langType;
	}

	public void setLangType(String langType) {
		this.langType = langType;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}



	public String getLoginSecret() {
		return loginSecret;
	}

	public void setLoginSecret(String loginSecret) {
		this.loginSecret = loginSecret;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public String getUserFrom() {
		return userFrom;
	}

	public void setUserFrom(String userFrom) {
		this.userFrom = userFrom;
	}

	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public String getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLastIp() {
		return lastIp;
	}

	public void setLastIp(String lastIp) {
		this.lastIp = lastIp;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [userId=").append(userId).append(", username=").append(username).append(", loginSecret=")
				.append(loginSecret).append(", password=").append(password).append(", masterUserId=")
				.append(masterUserId).append(", userType=").append(userType).append(", userPhone=").append(userPhone)
				.append(", userEmail=").append(userEmail).append(", nickName=").append(nickName).append(", createTime=")
				.append(createTime).append(", createBy=").append(createBy).append(", lastIp=").append(lastIp)
				.append(", lastTime=").append(lastTime).append(", userFrom=").append(userFrom).append(", userSex=")
				.append(userSex).append(", userPhoto=").append(userPhoto).append(", langType=").append(langType)
				.append("]");
		return builder.toString();
	}
}
