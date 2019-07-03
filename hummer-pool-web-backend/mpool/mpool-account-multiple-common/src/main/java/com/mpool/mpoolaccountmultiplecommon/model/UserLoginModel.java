package com.mpool.mpoolaccountmultiplecommon.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import java.util.Base64;
import java.util.Base64.Decoder;

/**
 * @author chenjian2
 *
 */
@ApiModel
public class UserLoginModel {
	@ApiModelProperty("用户名 邮箱 手机号码")
	private String username;

	@ApiModelProperty("密码")
	@NotEmpty
	private String password;

	private String telphoneCode;

	public String getTelphoneCode() {
		return telphoneCode;
	}

	public void setTelphoneCode(String telphoneCode) {
		this.telphoneCode = telphoneCode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		Decoder decoder = Base64.getDecoder();
		byte[] decode = decoder.decode(password);
		return new String(decode);
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
