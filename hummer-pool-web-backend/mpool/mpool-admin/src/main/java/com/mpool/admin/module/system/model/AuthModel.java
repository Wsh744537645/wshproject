package com.mpool.admin.module.system.model;

import java.util.Base64;

import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class AuthModel {
	@ApiModelProperty("用户名")
	private String username;
	@ApiModelProperty("密码")
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {

		return new String(Base64.getDecoder().decode(password));
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
