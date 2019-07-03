package com.mpool.common.model.account.bill;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 用户钱包（当日或者近几日的数据）对应表user_pay
 */

@TableName("user_pay")
public class UserPay {
	@TableId
	private Integer id;
	private Long puid;
	private String walletAddress;
	private Long totalDue; //总拥有
	private Long totalPaid; //总的支出
	private Integer payModel;
	private Integer pplnsRate;
	private Integer ppsplusRate;
	private Integer ppsplusGroup;
	private Integer fppsRate;
	private Integer fppsGroup;
	private Integer isPayEnable;
	private Date updateTime;
	private Date lastPayAt;
	private Long minPay;
	private Integer currencyId;

	public Long getPuid() {
		return puid;
	}

	public void setPuid(Long puid) {
		this.puid = puid;
	}

	public String getWalletAddress() {
		return walletAddress;
	}

	public void setWalletAddress(String walletAddress) {
		this.walletAddress = walletAddress;
	}

	public Long getTotalDue() {
		return totalDue;
	}

	public void setTotalDue(Long totalDue) {
		this.totalDue = totalDue;
	}

	public Long getTotalPaid() {
		return totalPaid;
	}

	public void setTotalPaid(Long totalPaid) {
		this.totalPaid = totalPaid;
	}

	public Integer getPayModel() {
		return payModel;
	}

	public void setPayModel(Integer payModel) {
		this.payModel = payModel;
	}

	public Integer getPplnsRate() {
		return pplnsRate;
	}

	public void setPplnsRate(Integer pplnsRate) {
		this.pplnsRate = pplnsRate;
	}

	public Integer getPpsplusRate() {
		return ppsplusRate;
	}

	public void setPpsplusRate(Integer ppsplusRate) {
		this.ppsplusRate = ppsplusRate;
	}

	public Integer getPpsplusGroup() {
		return ppsplusGroup;
	}

	public void setPpsplusGroup(Integer ppsplusGroup) {
		this.ppsplusGroup = ppsplusGroup;
	}

	public Integer getFppsRate() {
		return fppsRate;
	}

	public void setFppsRate(Integer fppsRate) {
		this.fppsRate = fppsRate;
	}

	public Integer getFppsGroup() {
		return fppsGroup;
	}

	public void setFppsGroup(Integer fppsGroup) {
		this.fppsGroup = fppsGroup;
	}

	public Integer getIsPayEnable() {
		return isPayEnable;
	}

	public void setIsPayEnable(Integer isPayEnable) {
		this.isPayEnable = isPayEnable;
	}

	public Date getLastPayAt() {
		return lastPayAt;
	}

	public void setLastPayAt(Date lastPayAt) {
		this.lastPayAt = lastPayAt;
	}

	public Long getMinPay() {
		return minPay;
	}

	public void setMinPay(Long minPay) {
		this.minPay = minPay;
	}

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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
