package com.mpool.common.model.account.fpps;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
@TableName("pool_fpps_record")
public class PoolFppsRecord {
	private Integer poolId;
	private Integer day;
	private Long fppsTotal;
	private Long fppsPayUser;
	private Long fppsPayPool;
	private Integer fppsUserCount;
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

	public Long getFppsTotal() {
		return fppsTotal;
	}

	public void setFppsTotal(Long fppsTotal) {
		this.fppsTotal = fppsTotal;
	}

	public Long getFppsPayUser() {
		return fppsPayUser;
	}

	public void setFppsPayUser(Long fppsPayUser) {
		this.fppsPayUser = fppsPayUser;
	}

	public Long getFppsPayPool() {
		return fppsPayPool;
	}

	public void setFppsPayPool(Long fppsPayPool) {
		this.fppsPayPool = fppsPayPool;
	}

	public Integer getFppsUserCount() {
		return fppsUserCount;
	}

	public void setFppsUserCount(Integer fppsUserCount) {
		this.fppsUserCount = fppsUserCount;
	}

	public Date getCreatAt() {
		return creatAt;
	}

	public void setCreatAt(Date creatAt) {
		this.creatAt = creatAt;
	}

}
