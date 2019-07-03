package com.mpool.common.model;

import java.util.Date;

public class AdminBaseEntity {
	protected Date createTime;

	protected Long createBy;

	protected Date lastupdateTime;

	protected Long lastupdateBy;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Date getLastupdateTime() {
		return lastupdateTime;
	}

	public void setLastupdateTime(Date lastupdateTime) {
		this.lastupdateTime = lastupdateTime;
	}

	public Long getLastupdateBy() {
		return lastupdateBy;
	}

	public void setLastupdateBy(Long lastupdateBy) {
		this.lastupdateBy = lastupdateBy;
	}

}
