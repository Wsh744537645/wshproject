package com.mpool.common.model.pool;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mpool.common.model.pool.utils.MathShare;
import com.mpool.common.model.pool.utils.MathShareDay;

/**
 * 用户day，用户每日矿机的信息，如算力、拒绝率等
 * 
 * @author chenjian2
 *
 */
@TableName("stats_users_day")
public class StatsUsersDay implements MathShareDay {

	private static final Logger log = LoggerFactory.getLogger(StatsUsersDay.class);

	private Long puid;
	private Long day;
	private Long shareAccept;
	private Long shareReject;
	private Double rejectRate;
	private BigDecimal score;
	private Long earn; //预计获得的btc数量
	private Date createdAt;
	private Date updatedAt;

	public Long getPuid() {
		return puid;
	}

	public void setPuid(Long puid) {
		this.puid = puid;
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

//	public double getHashAcceptT() {
//		return MathShare.MathShareDayDouble(shareAccept);
//	}
//
//	public double getHashRejectT() {
//		return MathShare.MathShareDayDouble(shareReject);
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
		result = prime * result + ((day == null) ? 0 : day.hashCode());
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
		StatsUsersDay other = (StatsUsersDay) obj;
		if (day == null) {
			if (other.day != null)
				return false;
		} else if (!day.equals(other.day))
			return false;
		return true;
	}



}
