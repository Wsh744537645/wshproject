package com.mpool.account.model;

import java.util.Base64;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class UserResetPasswordModel {

	@NotEmpty
	@ApiModelProperty("密码")
	private String password;

	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return new String(Base64.getDecoder().decode(password));
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
