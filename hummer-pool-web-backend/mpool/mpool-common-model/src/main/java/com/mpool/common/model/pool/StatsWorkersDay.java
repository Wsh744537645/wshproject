package com.mpool.common.model.pool;

import java.math.BigDecimal;
import java.util.Date;

import com.mpool.common.model.pool.utils.MathShare;
import com.mpool.common.model.pool.utils.MathShareDay;

/**
 * 矿工天
 * 
 * @author chenjian2
 *
 */
public class StatsWorkersDay implements MathShareDay {
	private Long puid;
	private Long workerId;
	private Long day;
	private Long shareAccept;
	private Long shareReject;
	private Double rejectRate;
	private BigDecimal score;
	private Long earn;
	private Date createdAt;
	private Date updatedAt;

//	public double getHashAcceptT() {
//		return MathShare.MathShareDayDouble(shareAccept);
//	}
//
//	public double getHashRejectT() {
//		return MathShare.MathShareDayDouble(shareReject);
//	}

	public Long getPuid() {
		return puid;
	}

	public void setPuid(Long puid) {
		this.puid = puid;
	}

	public Long getWorkerId() {
		return workerId;
	}

	public void setWorkerId(Long workerId) {
		this.workerId = workerId;
	}

	public Long getDay() {
		return day;
	}

	public void setDay(Long day) {
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
