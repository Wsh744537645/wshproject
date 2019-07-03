package com.mpool.account.exception;

public enum ExceptionCode {

	code_ex("mpool.account.code", "验证码错误！"), 
	email_code_err("mpool.account.mail.code", "邮箱验证码错误！"),
	username_null_err("mpool.account.username.null", "账号为空！"),
	email_not_find("mpool.account.mail.notfind", "邮箱未找到"),
	email_exists("mpool.account.mail.exists", "用户已被使用!"),
	username_exists("mpool.account.username.exists", "用户已被使用!"),
	phone_exists("mpool.account.phone.exists", "手机号码已被使用!"),
	username_or_password_err("mpool.account.login.auth", "用户名或者密码错误！"),
	account_ex("mpool.account.account.ex", "账号异常！"),
	login_ex("mpool.account.login.ex", "登录异常"), 
	switch_sub_account_ex("mpool.account.switch.sub.account.ex", "账号切换异常!"), 
	password_retry_too_many("mpool.account.password.retry.too.many","密码重试超出限制请稍后再试!"), 
	auth_not_perms_oper("mpool.account.auth.not.permission","无权限操作!"), 
	password_error("mpool.account.password.ex","密码错误"), 
	worker_ids_empty("mpool.account.worker.ids.empty","矿工Id为空!"), 
	user_wallet_empty("mpool.account.user.wallet.empty","钱包没有找到"),
	group_notfound("mpool.account.user.group.notfound", "矿工组没有找到!"), 
	worker_notfound("mpool.account.user.worker.notfound","矿工没有找到！"), 
	worker_in_group("mpool.account.delete.worker_in_group","当前组下还有矿工!"), 
	login_code_empty("mpool.account.login.code.empty","请输短信证码"), 
	username_or_code_err("mpool.account.auth.username_code.err","用户名或者验证码错误"), 
	worker_group_exist("mpool.account.user.worker.group.exist","矿工组重复"), 
	wokername_exist("mpool.account.user.worker.name.exist", "矿工名不可用"),
	data_invalid("mpool.account.data.invalid", "非法的数据"),
	worker_name_empty("mpool.account.worker.name.empty","矿工名不能为空"), 
	param_ex("mpool.account.param.ex","参数异常"), 
	coin_error("mpool.account.coin.error","地址错误"),
	phone_not_exists("mpool.account.phone.not.exist","手机未注册"),
	user_not_exists("mpool.account.user.not.exist","用户不存在!"),
	multiple_service_not_exists("com.mpool.account.controller.MutipleController.not.exist", "该币种服务不存在"),
	account_not_exists("com.mpool.account.controller.UsermanagerController.createPay", "账户不存在"),
	currency_type_not_exists("om.mpool.account.service.bill.impl.UserPayServiceImpl", "暂不支持该币种"),
	user_currency_wallet_exists("mpool.account.user.wallet.currency.exists","该币种钱包已经存在");


	private String code;

	private String msg;

	private ExceptionCode(String code, String msg) {
		this.code = code;
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
