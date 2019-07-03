package com.mpool.common.model.account.fpps;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mpool.common.model.pool.utils.MathShare;

/**
 *  用户矿机产生的收益记录，对应表 user_fpps_record
 */

@TableName("user_fpps_record")
public class UserFppsRecord {
	private final static int SHARE_WINDOW = 24 * 60 * 60;
	protected Long puid;
	protected Integer day;
	protected Long fppsBeforeFee;//before是预计收益
	protected Long fppsAfterFee;//实际收益
	protected Integer fppsRate;
	protected Date creatAt;
	protected Long share;

	public UserFppsRecord(Long puid, Integer day, Long share, Long fppsBeforeFee, Long fppsAfterFee, Integer fppsRate,
			Date creatAt) {
		super();
		this.share = share;
		this.puid = puid;
		this.day = day;
		this.fppsBeforeFee = fppsBeforeFee;
		this.fppsAfterFee = fppsAfterFee;
		this.fppsRate = fppsRate;
		this.creatAt = creatAt;
	}

	public UserFppsRecord() {
		super();
	}

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

	public Long getFppsBeforeFee() {
		return fppsBeforeFee;
	}

	public void setFppsBeforeFee(Long fppsBeforeFee) {
		this.fppsBeforeFee = fppsBeforeFee;
	}

	public Long getFppsAfterFee() {
		return fppsAfterFee;
	}

	public void setFppsAfterFee(Long fppsAfterFee) {
		this.fppsAfterFee = fppsAfterFee;
	}

	public Integer getFppsRate() {
		return fppsRate;
	}

	public void setFppsRate(Integer fppsRate) {
		this.fppsRate = fppsRate;
	}

	public Date getCreatAt() {
		return creatAt;
	}

	public void setCreatAt(Date creatAt) {
		this.creatAt = creatAt;
	}

	public Long getShare() {
		return share;
	}

	public void setShare(Long share) {
		this.share = share;
	}

	public double getDayShareT() {
		return MathShare.mathShareDouble(this.share, SHARE_WINDOW);
	}
}
