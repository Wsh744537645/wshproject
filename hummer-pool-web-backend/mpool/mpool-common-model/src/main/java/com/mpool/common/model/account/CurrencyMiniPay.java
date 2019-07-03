package com.mpool.common.model.account;

import static com.mpool.common.model.constant.AccountConstant.TABLE_PREFIX;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(TABLE_PREFIX + "currency_mini_pay")
public class CurrencyMiniPay {
	@TableId
	private Integer id;

	private Integer currencyId;
	private BigDecimal miniPay;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public BigDecimal getMiniPay() {
		return miniPay;
	}

	public void setMiniPay(BigDecimal miniPay) {
		this.miniPay = miniPay;
	}

}
