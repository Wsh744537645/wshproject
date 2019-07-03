package com.mpool.mpoolaccountmultiplecommon.exception;

import com.mpool.common.exception.MpoolException;

public class AccountException extends MpoolException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3138889213090755720L;

	public AccountException(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public AccountException(ExceptionCode ecode) {
		this.code = ecode.getCode();
		this.msg = ecode.getMsg();
	}

	public static AccountException paramEx() {
		return new AccountException("400", "参数错误!");
	}

	public static AccountException security() {
		return new AccountException("account.security.ex", "安全认证失败");
	}

	public static AccountException userNotfound() {
		return new AccountException("user.not.found", "用户没有找到!");
	}
}
