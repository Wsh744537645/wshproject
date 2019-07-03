package com.mpool.share.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class UserModel {
	private Long userId;

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
	 * 头像
	 */
	private String photo;

	private String walletAddress;

	private String token;

}
