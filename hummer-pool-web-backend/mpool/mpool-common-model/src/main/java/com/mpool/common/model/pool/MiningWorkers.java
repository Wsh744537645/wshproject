package com.mpool.common.model.pool;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.mpool.common.model.pool.utils.MathShare;
@TableName("mining_workers")
public class MiningWorkers {
	protected String workerId;
	@JsonAlias(value = "userId")
	protected Long puid;
	protected Long groupId;
	protected String workerName;
	protected Long accept1m;
	protected Long accept5m;
	protected Long accept15m;
	protected Long reject15m;
	protected Long accept1h;
	protected Long reject1h;
	protected Integer acceptCount;
	protected String lastShareIp;
	protected Date lastShareTime;
	protected String minerAgent;
	protected Date createdAt;
	protected Date updatedAt;

	public String getStatus() {
		String status = null;
		if (accept1m == null) {
			status = "deletion";
		} else {
			if (accept15m == null || accept15m == 0) {
				status = "Inactive";
			} else {
				status = "active";
			}
		}
//		if ("Online".equals(status)) {
//			if (accept1m > 0) {
//				status = "active";
//			}
//		}
		return status;
	}

	private static final int M5 = 60 * 5;
	private static final int M15 = 60 * 15;

	public Double getHashAccept1mT() {
		return MathShare.MathShareMinuteDouble(accept1m);
	}

	public Double getHashAccept5mT() {
		return MathShare.mathShareDouble(accept5m, M5);
	}

	public Double getHashAccept15mT() {
		return MathShare.mathShareDouble(accept15m, M15);
	}

	public Double getHashReject15mT() {
		return MathShare.mathShareDouble(reject15m, M15);
	}

	public Double getHashAccept1hT() {
		return MathShare.MathShareHourDouble(accept1h);
	}

	public Double getHashReject1hT() {
		return MathShare.MathShareHourDouble(reject1h);
	}

	public String getWorkerId() {
		return workerId;
	}

	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}

	public Long getPuid() {
		return puid;
	}

	public void setPuid(Long puid) {
		this.puid = puid;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public Long getAccept1m() {
		return accept1m;
	}

	public void setAccept1m(Long accept1m) {
		this.accept1m = accept1m;
	}

	public Long getAccept5m() {
		return accept5m;
	}

	public void setAccept5m(Long accept5m) {
		this.accept5m = accept5m;
	}

	public Long getAccept15m() {
		return accept15m;
	}

	public void setAccept15m(Long accept15m) {
		this.accept15m = accept15m;
	}

	public Long getReject15m() {
		return reject15m;
	}

	public void setReject15m(Long reject15m) {
		this.reject15m = reject15m;
	}

	public Long getAccept1h() {
		return accept1h;
	}

	public void setAccept1h(Long accept1h) {
		this.accept1h = accept1h;
	}

	public Long getReject1h() {
		return reject1h;
	}

	public void setReject1h(Long reject1h) {
		this.reject1h = reject1h;
	}

	public Integer getAcceptCount() {
		return acceptCount;
	}

	public void setAcceptCount(Integer acceptCount) {
		this.acceptCount = acceptCount;
	}

	public String getLastShareIp() {
		return lastShareIp;
	}

	public void setLastShareIp(String lastShareIp) {
		this.lastShareIp = lastShareIp;
	}

	public Date getLastShareTime() {
		return lastShareTime;
	}

	public void setLastShareTime(Date lastShareTime) {
		this.lastShareTime = lastShareTime;
	}

	public String getMinerAgent() {
		return minerAgent;
	}

	public void setMinerAgent(String minerAgent) {
		this.minerAgent = minerAgent;
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
		result = prime * result + ((puid == null) ? 0 : puid.hashCode());
		result = prime * result + ((workerId == null) ? 0 : workerId.hashCode());
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
		MiningWorkers other = (MiningWorkers) obj;
		if (puid == null) {
			if (other.puid != null)
				return false;
		} else if (!puid.equals(other.puid))
			return false;
		if (workerId == null) {
			if (other.workerId != null)
				return false;
		} else if (!workerId.equals(other.workerId))
			return false;
		return true;
	}
}
