package com.mpool.common.model.account.fpps;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("user_ppsplus_record")
public class UserPpsplusRecord {
	private Long puid;
	private Integer day;
	private Long ppsplusBeforeFee;
	private Long ppsplusAfterFee;
	private Integer ppsplusRate;
	private Date creatAt;

	public Long getPuid() {
		return puid;
	}

	public void setPuid(Long puid) {
		this.puid = puid;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Long getPpsplusBeforeFee() {
		return ppsplusBeforeFee;
	}

	public void setPpsplusBeforeFee(Long ppsplusBeforeFee) {
		this.ppsplusBeforeFee = ppsplusBeforeFee;
	}

	public Long getPpsplusAfterFee() {
		return ppsplusAfterFee;
	}

	public void setPpsplusAfterFee(Long ppsplusAfterFee) {
		this.ppsplusAfterFee = ppsplusAfterFee;
	}

	public Integer getPpsplusRate() {
		return ppsplusRate;
	}

	public void setPpsplusRate(Integer ppsplusRate) {
		this.ppsplusRate = ppsplusRate;
	}

	public Date getCreatAt() {
		return creatAt;
	}

	public void setCreatAt(Date creatAt) {
		this.creatAt = creatAt;
	}

}
