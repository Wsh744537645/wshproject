package com.mpool.account.model;

import java.util.Base64;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class UserRegModel {
	@ApiModelProperty(value = "密码")
	@NotEmpty
	private String password;
	@ApiModelProperty(value = "用户名")
	@NotEmpty
	private String username;

	public String getPassword() {

		return new String(Base64.getDecoder().decode(password));
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
