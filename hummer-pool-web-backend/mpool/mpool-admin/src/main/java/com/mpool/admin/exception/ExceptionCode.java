package com.mpool.admin.exception;

public enum ExceptionCode {

	code_ex("code", "验证码错误！"), 
	email_code_err("mail.code", "邮箱验证码错误！"),
	username_null_err("username.null", "账号为空！"),
	email_not_find("mail.notfind", "邮箱未找到"),
	email_exists("mail.exists", "用户已被使用!"),
	username_exists("username.exists", "用户已被使用!"),
	phone_exists("username.exists", "手机号码已被使用!"),
	username_or_password_err("login.auth", "用户名或者密码错误！"),
	account_ex("account.ex", "账号异常！"),
	login_ex("login.ex", "登录异常"), 
	switch_sub_account_ex("switch.sub.account.ex", "账号切换异常!"), 
	password_retry_too_many("password.retry.too.many","密码重试超出限制请稍后再试!"), 
	auth_not_perms_oper("auth.not.permission","无权限操作!"), 
	password_error("password.ex","密码错误"), 
	worker_ids_empty("worker.ids.empty","矿工Id为空!"), 
	user_wallet_empty("user.wallet.empty","钱包没有找到"),
	group_notfound("user.group.notfound", "矿工组没有找到!"), 
	worker_notfound("user.worker.notfound","矿工没有找到！"), 
	worker_in_group("delete.worker_in_group","当前组下还有矿工!"), 
	login_code_empty("login.code.empty","请输短信证码"), 
	username_or_code_err("auth.username_code.err","用户名或者验证码错误"), 
	worker_group_exist("user.worker.group.exist","矿工组重复"), 
	wokername_exist("user.worker.name.exist", "矿工名不可用"),
	data_invalid("data.invalid", "非法的数据"),
	worker_name_empty("worker.name.empty","矿工名不能为空"), 
	param_ex("param.ex","参数异常"),
	not_member_user("not.member.user","非子账户"),
	admin_exists("admin.exists","管理员不存在"),
	admin_close("admin.close","该账户已被锁定"),
	menu_exists("menu.exists","菜单已存在"),
	service_not_exist("currency.not.exists", "币种服务不存在");

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
