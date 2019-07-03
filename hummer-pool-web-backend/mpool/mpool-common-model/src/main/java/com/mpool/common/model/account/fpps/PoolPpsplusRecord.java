package com.mpool.common.model.account.fpps;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
@TableName("pool_ppsplus_record")
public class PoolPpsplusRecord {
	private Integer poolId;
	private Integer day;
	private Long ppsplusTotal;
	private Long ppsplusPayUser;
	private Long ppsplusPayPool;
	private Integer ppsplusUserCount;
	private Date creatAt;

	public Integer getPoolId() {
		return poolId;
	}

	public void setPoolId(Integer poolId) {
		this.poolId = poolId;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Long getPpsplusTotal() {
		return ppsplusTotal;
	}

	public void setPpsplusTotal(Long ppsplusTotal) {
		this.ppsplusTotal = ppsplusTotal;
	}

	public Long getPpsplusPayUser() {
		return ppsplusPayUser;
	}

	public void setPpsplusPayUser(Long ppsplusPayUser) {
		this.ppsplusPayUser = ppsplusPayUser;
	}

	public Long getPpsplusPayPool() {
		return ppsplusPayPool;
	}

	public void setPpsplusPayPool(Long ppsplusPayPool) {
		this.ppsplusPayPool = ppsplusPayPool;
	}

	public Integer getPpsplusUserCount() {
		return ppsplusUserCount;
	}

	public void setPpsplusUserCount(Integer ppsplusUserCount) {
		this.ppsplusUserCount = ppsplusUserCount;
	}

	public Date getCreatAt() {
		return creatAt;
	}

	public void setCreatAt(Date creatAt) {
		this.creatAt = creatAt;
	}
}
