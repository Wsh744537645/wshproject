package com.mpool.common.model.account;

import static com.mpool.common.model.constant.AccountConstant.TABLE_PREFIX;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(TABLE_PREFIX + "worker__worker_group")
public class WorkerGroupWorker {
	private Long workerId;
	private Long userId;
	private Long groupId;

	public Long getWorkerId() {
		return workerId;
	}

	public void setWorkerId(Long workerId) {
		this.workerId = workerId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

}
