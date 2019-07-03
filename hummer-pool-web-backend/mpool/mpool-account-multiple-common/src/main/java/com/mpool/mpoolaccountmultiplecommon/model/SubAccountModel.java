package com.mpool.mpoolaccountmultiplecommon.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.Base64;

/**
 * @author chen 创建子账号
 */
@ApiModel
public class SubAccountModel {
	@ApiModelProperty("用户名")
	private String username;
	@ApiModelProperty("密码")
	private String password;
	@ApiModelProperty("是否拷贝主账号邮箱和手机")
	private Boolean copyMaster;
	@ApiModelProperty("手机")
	@Pattern(regexp = "^1[1-9]\\d{9}$", message = "请输入正确手机号码")
	private String phone;
	@ApiModelProperty("邮箱")
	@Email
	private String mail;
	@ApiModelProperty("区域")
	private Integer regionId;
	@ApiModelProperty("钱包信息")
	private WalletModel[] wallets;
	@ApiModelProperty("打款方式")
	private String payType;

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		byte[] decode = Base64.getDecoder().decode(password);
		return new String(decode);
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public Boolean getCopyMaster() {
		return copyMaster;
	}

	public void setCopyMaster(Boolean copyMaster) {
		this.copyMaster = copyMaster;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public WalletModel[] getWallets() {
		return wallets;
	}

	public void setWallets(WalletModel[] wallets) {
		this.wallets = wallets;
	}

}
