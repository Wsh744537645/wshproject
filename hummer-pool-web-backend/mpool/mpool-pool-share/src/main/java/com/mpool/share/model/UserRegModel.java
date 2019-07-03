package com.mpool.share.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import java.util.Base64;

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
