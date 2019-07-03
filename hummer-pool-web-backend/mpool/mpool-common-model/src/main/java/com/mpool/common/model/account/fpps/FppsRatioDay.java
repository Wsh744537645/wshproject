package com.mpool.common.model.account.fpps;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("fpps_ratio_day")
public class FppsRatioDay {
	@TableId(type = IdType.INPUT)
	protected Integer day;
	protected Float newRatio;
	protected Float ratio;
	protected Integer heightBegin;
	protected Integer heightEnd;
	protected Integer heightNum;

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Float getRatio() {
		return ratio;
	}

	public void setRatio(Float ratio) {
		this.ratio = ratio;
	}

	public Integer getHeightBegin() {
		return heightBegin;
	}

	public void setHeightBegin(Integer heightBegin) {
		this.heightBegin = heightBegin;
	}

	public Integer getHeightEnd() {
		return heightEnd;
	}

	public void setHeightEnd(Integer heightEnd) {
		this.heightEnd = heightEnd;
	}

	public Integer getHeightNum() {
		return heightNum;
	}

	public void setHeightNum(Integer heightNum) {
		this.heightNum = heightNum;
	}

	public Float getNewRatio() {
		return newRatio;
	}

	public void setNewRatio(Float newRatio) {
		this.newRatio = newRatio;
	}
}
