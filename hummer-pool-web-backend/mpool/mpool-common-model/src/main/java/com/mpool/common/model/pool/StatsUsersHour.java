package com.mpool.common.model.pool;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mpool.common.model.pool.utils.MathShare;
import com.mpool.common.model.pool.utils.MathShareHour;

/**
 * 用户数据小时
 * 
 * @author chenjian2
 *
 */
public class StatsUsersHour implements MathShareHour {

	private static final Logger log = LoggerFactory.getLogger(StatsUsersHour.class);

	private Long puid;
	private Long hour;
	private Long shareAccept;
	private Long shareReject;
	private Double rejectRate;
	private BigDecimal score;
	private Long earn;
	private Date createdAt;
	private Date updatedAt;

	public Long getPuid() {
		return puid;
	}

	public void setPuid(Long puid) {
		this.puid = puid;
	}

	public Long getHour() {
		return hour;
	}

	public void setHour(Long hour) {
		this.hour = hour;
	}

	public Long getShareAccept() {
		return shareAccept;
	}

//	public double getHashRateAcceptT() {
//		return MathShare.MathShareHourDouble(shareAccept);
//	}
//
//	public double getHashRejectT() {
//		return MathShare.MathShareHourDouble(shareReject);
//	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hour == null) ? 0 : hour.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StatsUsersHour other = (StatsUsersHour) obj;
		if (hour == null) {
			if (other.hour != null)
				return false;
		} else if (!hour.equals(other.hour))
			return false;
		return true;
	}




}
