package com.mpool.admin.exception;

import com.mpool.common.exception.MpoolException;

public class AdminException extends MpoolException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5847982644563276987L;

	public AdminException(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public AdminException(ExceptionCode ecode) {
		this.code = ecode.getCode();
		this.msg = ecode.getMsg();
	}
}
