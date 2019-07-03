package com.mpool.common;

public class Result {

	private String msg;
	private String code;
	private Object data;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Result(Object data) {
		this.data = data;
	}

	public Result() {
	}

	public static Result ok() {
		Result result = new Result();
		result.setCode("0");
		result.setMsg("ok");
		return result;
	}

	public static Result ok(Object data) {
		Result result = new Result();
		result.setCode("0");
		result.setMsg("ok");
		result.setData(data);
		return result;
	}

	public static Result err() {
		Result result = new Result();
		result.setCode("-1");
		result.setMsg("err");
		result.setData("Authentication failure!");
		return result;
	}

	public static Result err401() {
		Result result = new Result();
		result.setCode("401");
		result.setMsg("err");
		result.setData("Authentication failure!");
		return result;
	}

	public static Result err(Object data) {
		Result result = new Result();
		result.setCode("-2");
		result.setMsg("err");
		result.setData(data);
		return result;
	}
}
