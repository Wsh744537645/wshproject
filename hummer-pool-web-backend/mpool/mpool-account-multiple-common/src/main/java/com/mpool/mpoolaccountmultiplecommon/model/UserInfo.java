package com.mpool.mpoolaccountmultiplecommon.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.Base64;

public class UserInfo {
	private String username;
	private String password;
	@Email
	private String email;
	@Pattern(regexp = "^1[1-9]\\d{9}$", message = "phone.error")
	private String phone;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		password = new String(Base64.getDecoder().decode(password));
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

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

}
