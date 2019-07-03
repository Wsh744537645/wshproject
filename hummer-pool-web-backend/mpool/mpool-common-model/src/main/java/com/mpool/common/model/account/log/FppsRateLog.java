package com.mpool.common.model.account.log;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("log_fpps_rate")
public class FppsRateLog {
	@TableId
	private Integer id;

	private Integer tagetId;

	private Integer oldFppsRate;

	private Integer newFppsRate;

	private Long puid;

	private Long updateBy;

	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTagetId() {
		return tagetId;
	}

	public void setTagetId(Integer tagetId) {
		this.tagetId = tagetId;
	}

	public Integer getOldFppsRate() {
		return oldFppsRate;
	}

	public void setOldFppsRate(Integer oldFppsRate) {
		this.oldFppsRate = oldFppsRate;
	}

	public Integer getNewFppsRate() {
		return newFppsRate;
	}

	public void setNewFppsRate(Integer newFppsRate) {
		this.newFppsRate = newFppsRate;
	}

	public Long getPuid() {
		return puid;
	}

	public void setPuid(Long puid) {
		this.puid = puid;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
