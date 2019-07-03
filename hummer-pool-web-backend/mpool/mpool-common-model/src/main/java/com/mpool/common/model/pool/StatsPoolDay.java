package com.mpool.common.model.pool;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baomidou.mybatisplus.annotation.TableId;
import com.mpool.common.model.pool.utils.MathShare;
import com.mpool.common.model.pool.utils.MathShareDay;

/**
 * 池数据天
 * 
 * @author chenjian2
 *
 */
public class StatsPoolDay implements MathShareDay, Serializable {
	private static final long serialVersionUID = 5971317838809719556L;
	@TableId
	private Integer day;
	private Long shareAccept;
	private Long shareReject;
	private Double rejectRate;
	private BigDecimal score;
	private Long earn;
	private Double lucky;
	private Date createdAt;
	private Date updatedAt;

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

//	public double getHashAcceptT() {
//		return MathShare.MathShareDayDouble(shareAccept);
//	}
//
//	public double getHashRejectT() {
//		return MathShare.MathShareDayDouble(shareReject);
//	}

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
