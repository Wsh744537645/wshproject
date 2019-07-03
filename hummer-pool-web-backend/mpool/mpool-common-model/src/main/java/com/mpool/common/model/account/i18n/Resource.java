package com.mpool.common.model.account.i18n;

import static com.mpool.common.model.constant.AccountConstant.TABLE_PREFIX;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(TABLE_PREFIX + "resource")
public class Resource {

	@TableId
	private Integer resId;

	private String code;

	private String message;

	private String locale;

	public Integer getResId() {
		return resId;
	}

	public void setResId(Integer resId) {
		this.resId = resId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

}
