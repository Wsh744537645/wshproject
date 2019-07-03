package com.mpool.admin.module.bill.model;

import java.math.BigDecimal;
import java.util.Date;

public class BillItemCSVModel {
	private static final double BTC = Math.pow(10, 8);
	private Integer id;
	private String username;
	private String billNumber;
	private Integer day;
	private String walletAddress;
	private Date cteateAt;
	private BigDecimal payBtc;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public String getWalletAddress() {
		return walletAddress;
	}

	public void setWalletAddress(String walletAddress) {
		this.walletAddress = walletAddress;
	}

	public Date getCteateAt() {
		return cteateAt;
	}

	public void setCteateAt(Date cteateAt) {
		this.cteateAt = cteateAt;
	}

	public BigDecimal getPayBtc() {
		return payBtc.divide(BigDecimal.valueOf(BTC));
	}

	public void setPayBtc(BigDecimal payBtc) {
		this.payBtc = payBtc;
	}

}
