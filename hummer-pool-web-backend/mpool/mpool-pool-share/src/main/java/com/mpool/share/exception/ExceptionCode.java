package com.mpool.share.exception;

public enum ExceptionCode {

	code_ex("code", "验证码错误！"), 
	email_code_err("mail.code", "邮箱验证码错误！"),
	username_null_err("username.null", "账号为空！"),
	email_not_find("mail.notfind", "邮箱未找到"),
	email_exists("mail.exists", "用户已被使用!"),
	username_exists("username.exists", "用户已被使用!"),
	phone_exists("username.exists", "手机号码已被使用!"),
	phone_not_find("tel.not.exists", "手机号未找到"),
	username_or_password_err("login.auth", "用户名或者密码错误！"),
	account_ex("account.ex", "账号异常！"),
	login_ex("login.ex", "登录异常"), 
	switch_sub_account_ex("switch.sub.account.ex", "账号切换异常!"),
	phone_not_exists("mpool.account.phone.not.exist","手机未注册"),
	user_not_exists("mpool.account.user.not.exist","用户不存在!"),
	login_code_empty("mpool.account.login.code.empty","请输短信证码"),
	username_or_code_err("mpool.account.auth.username_code.err","用户名或者验证码错误"),
	password_retry_too_many("password.retry.too.many","密码重试超出限制请稍后再试!"), 
	auth_not_perms_oper("auth.not.permission","无权限操作!"), 
	password_error("password.ex","密码错误"),
	pay_type_error("pay_type", "支付方式不存在"),
	product_not_exist("product_not_exist", "商品不存在"),
	product_stock_not_enough("product_stock_not_enough", "商品库存不足"),
	btc_2_usd_zero("btc_2_usd_zero", "汇率获取异常"),
	order_id_not_exists("order_id_not_exists", "订单号不存在"),
	wallet_address_error("wallet_address_error", "钱包地址错误"),
	param_ex("param.ex","参数异常");

	private String code;

	private String msg;

	private ExceptionCode(String code, String msg) {
		this.code = "mpool.admin."+code;
		this.msg = msg;
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
