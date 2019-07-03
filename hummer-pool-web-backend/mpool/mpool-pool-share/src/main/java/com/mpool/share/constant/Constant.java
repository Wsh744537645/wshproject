package com.mpool.share.constant;

public class Constant {
	public final static String C_SUB_USER_KEY = "subUser";

	/**
	 * 登录 验证码前缀
	 */
	public final static String CODE_LOGIN_PREFIX = "login:";

	/**
	 * 注册邮箱验证码前缀
	 */
	public final static String CODE_REG_PREFIX = "user:reg:";
	/**
	 * 注册邮箱验证码前缀
	 */
	public final static String CODE_REST_PASSWORD_PREFIX = "user:rest:";

	/**
	 * 手机验证码登录
	 */
	public final static String CODE_SMS_LOGIN_PREFIX = "telphone:login:";
	/**
	 * 发送邮箱接口验证码前缀
	 */
	public final static String CODE_EMAIL_PREFIX = "sent:email:";

	/**
	 * 发送短信接口的验证码前缀
	 */
	public final static String CODE_PHONE_PREFIX = "sent:phone:";

	/**
	 * 修改邮箱-发送验证码
	 */
	public final static String CODE_MAIL_SETTING_PREFIX = "send:setting:email:";

	/**
	 * 修改手机号-发送验证码
	 */
	public final static String CODE_PHONE_SETTING_PREFIX = "send:setting:phone:";

	/**
	 * 修改钱包地址-发送邮箱验证码
	 */
	public final static String CODE_MAIL_WALLET_PREFIX = "send:wallet:email:";

	/**
	 * 修改钱包地址-发送手机验证码
	 */
	public final static String CODE_PHONE_WALLET_PREFIX = "send:wallet:phone:";

    /**
     * 修改用户密码-发送邮箱验证码
     */
    public final static String CODE_MAIL_PASSWORD_PREFIX = "send:wallet:email:";

    /**
     * 修改用户密码-发送手机验证码
     */
    public final static String CODE_PHONE_PASSWORD_PREFIX = "send:wallet:phone:";

	/**
	 * 最小支付 pow(10,8)
	 */
	public final static double MIN_PAY = Math.pow(10, 8);

	/**
	 * 矿工状态
	 * 
	 * @author chenjian2
	 *
	 */
	public enum WorkerStatus {
		active(0), offline(2), inactive(1), all(null);
		private Integer status;

		private WorkerStatus(Integer status) {
			this.status = status;
		}

		public Integer getStatus() {
			return status;
		}
	}

	public final static String securityCode = "SecurityCode";

	/**
	 * 发送验证码方式
	 * 
	 * @author chenjian2
	 *
	 */
	public enum ReceiveCode {
		phone, email;
	}

	public final static String PHONE_REG_EXP = "^1[1-9]\\\\d{9}$";
}
