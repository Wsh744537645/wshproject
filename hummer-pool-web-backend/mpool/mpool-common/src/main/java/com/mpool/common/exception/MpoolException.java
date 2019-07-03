package com.mpool.common.exception;

public class MpoolException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8344787468110568021L;

	protected String code;

	protected String msg;

	public MpoolException(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public MpoolException() {
		super();
		// TODO Auto-generated constructor stub
	}


	@Override
	public String getMessage() {
		return msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
