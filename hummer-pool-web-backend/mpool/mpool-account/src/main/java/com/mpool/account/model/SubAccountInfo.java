package com.mpool.account.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class SubAccountInfo {
	@ApiModelProperty("子账户")
	private String username;
	@ApiModelProperty("区域")
	private String regionName;
	@ApiModelProperty("钱包信息")
	private WalletModel[] wallets;
	private String userPhone;
	private String userEmail;

	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public WalletModel[] getWallets() {
		return wallets;
	}

	public void setWallets(WalletModel[] wallets) {
		this.wallets = wallets;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
}
