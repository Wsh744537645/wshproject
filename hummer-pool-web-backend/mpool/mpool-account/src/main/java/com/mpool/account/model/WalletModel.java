package com.mpool.account.model;

import java.math.BigDecimal;

import com.mpool.account.constant.Constant;
import com.mpool.common.model.account.bill.UserPay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class WalletModel {

	@ApiModelProperty("区域")
	private String regionName;
	@ApiModelProperty("钱包Id")
	private Integer walletId;
	@ApiModelProperty("钱包类型")
	private String walletType;
	@ApiModelProperty("钱包地址")
	private String walletAddr;
	@ApiModelProperty("最小打款")
	private BigDecimal miniPay;
	@ApiModelProperty("结算方式")
	private String payType;
	@ApiModelProperty("邮箱")
	private String email;
	@ApiModelProperty("手机号码")
	private String phone;

	public WalletModel(UserPay btcUserPay) {
		this.walletId = btcUserPay.getId();
		this.miniPay = new BigDecimal(btcUserPay.getMinPay()).divide(BigDecimal.valueOf(Constant.MIN_PAY));
		this.walletAddr = btcUserPay.getWalletAddress();
		this.walletType = "BTC";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public WalletModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getWalletId() {
		return walletId;
	}

	public void setWalletId(Integer walletId) {
		this.walletId = walletId;
	}

	public String getWalletType() {
		return walletType;
	}

	public void setWalletType(String walletType) {
		this.walletType = walletType;
	}

	public String getWalletAddr() {
		return walletAddr;
	}

	public void setWalletAddr(String walletAddr) {
		this.walletAddr = walletAddr;
	}

	public BigDecimal getMiniPay() {
		return miniPay;
	}

	public void setMiniPay(BigDecimal miniPay) {
		this.miniPay = miniPay;
	}

}