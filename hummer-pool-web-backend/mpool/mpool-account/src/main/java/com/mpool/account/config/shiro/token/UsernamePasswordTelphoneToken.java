package com.mpool.account.config.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;

public class UsernamePasswordTelphoneToken extends UsernamePasswordToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1185887821924244295L;

	private String telphoneCode;

	public String getTelphoneCode() {
		return telphoneCode;
	}

	public UsernamePasswordTelphoneToken(final String telphoneCode) {
		super();
		this.telphoneCode = telphoneCode;
	}

	public UsernamePasswordTelphoneToken(final String username, final String password, final String telphoneCode) {
		super(username, password);
		this.telphoneCode = telphoneCode;
	}


	public UsernamePasswordTelphoneToken(final String username, final String password) {
		super(username, password);
	}
}
