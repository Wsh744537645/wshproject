package com.mpool.admin.InModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class InBlockchain {

	// 每天全网算力
	private BigDecimal hashRate;
	// 每天全网爆块数
	private int blocks;
	// 过去的每一天
	private String createdDay;
	// 我们自己的当前24小时算力
	private BigDecimal currentHashRate;
	// 全网算力占比
	private BigDecimal rate;

	// stats表
	private Integer day;
	private Long shareAccept;
	private Long shareReject;
	private Double rejectRate;
	private BigDecimal score;
	private Long earn;
	private Double lucky;
	private Date createdAt;
	private Date updatedAt;

	public BigDecimal getHashRate() {
		return hashRate;
	}

	public void setHashRate(BigDecimal hashRate) {
		this.hashRate = hashRate;
	}

	public int getBlocks() {
		return blocks;
	}

	public void setBlocks(int blocks) {
		this.blocks = blocks;
	}

	public String getCreatedDay() {
		return createdDay;
	}

	public void setCreatedDay(String createdDay) {
		this.createdDay = createdDay;
	}

	public BigDecimal getCurrentHashRate() {
		return currentHashRate;
	}

	public void setCurrentHashRate(BigDecimal currentHashRate) {
		this.currentHashRate = currentHashRate;
	}

	public BigDecimal getRate() {

		// 获取占比 我们的算力除以全网的算力
		if (currentHashRate == null || hashRate == null) {
			return new BigDecimal(0);
		}
		if (hashRate.compareTo(BigDecimal.ZERO) == 0) {
			return new BigDecimal(0);
		}

//		BigDecimal rates = hashRate.divide(BigDecimal.valueOf(1000));

		BigDecimal rate = currentHashRate.divide(hashRate, 3, RoundingMode.HALF_UP);

		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Long getShareAccept() {
		return shareAccept;
	}

	public void setShareAccept(Long shareAccept) {
		this.shareAccept = shareAccept;
	}

	public Long getShareReject() {
		return shareReject;
	}

	public void setShareReject(Long shareReject) {
		this.shareReject = shareReject;
	}

	public Double getRejectRate() {
		return rejectRate;
	}

	public void setRejectRate(Double rejectRate) {
		this.rejectRate = rejectRate;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public Long getEarn() {
		return earn;
	}

	public void setEarn(Long earn) {
		this.earn = earn;
	}

	public Double getLucky() {
		return lucky;
	}

	public void setLucky(Double lucky) {
		this.lucky = lucky;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
}
