package com.mpool.common.model.account.fpps;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("user_rate_day")
public class UserRateDay {
	private Long puid;
	private Integer day;
	private Integer fppsRate;

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

	public Integer getFppsRate() {
		return fppsRate;
	}

	public void setFppsRate(Integer fppsRate) {
		this.fppsRate = fppsRate;
	}

}
