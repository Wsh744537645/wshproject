package com.mpool.common.model.account;

import static com.mpool.common.model.constant.AccountConstant.TABLE_PREFIX;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(TABLE_PREFIX + "language")
public class Language {
	@TableId
	private Integer langId;
	/**
	 * 语言类型
	 */
	private String langType;

	/**
	 * 语言key
	 */
	private String langKey;

	/**
	 * 内容
	 */
	private String langValue;

	public Integer getLangId() {
		return langId;
	}

	public void setLangId(Integer langId) {
		this.langId = langId;
	}

	public String getLangType() {
		return langType;
	}

	public void setLangType(String langType) {
		this.langType = langType;
	}

	public String getLangKey() {
		return langKey;
	}

	public void setLangKey(String langKey) {
		this.langKey = langKey;
	}

	public String getLangValue() {
		return langValue;
	}

	public void setLangValue(String langValue) {
		this.langValue = langValue;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SysLang [langId=").append(langId).append(", langType=").append(langType).append(", langKey=")
				.append(langKey).append(", langValue=").append(langValue).append("]");
		return builder.toString();
	}

}
