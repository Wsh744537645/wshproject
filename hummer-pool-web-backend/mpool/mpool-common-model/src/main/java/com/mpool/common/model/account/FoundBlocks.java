package com.mpool.common.model.account;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("found_blocks")
public class FoundBlocks {
	protected Integer id;
	protected Long puid;
	protected Long workerId;
	protected String workerFullName;// varchar(50)
	protected String jobId;// bigint(20) unsigned
	protected Integer height;// int(11)
	protected Integer isOrphaned;// tinyint(4)
	protected String hash;// char(64)
	protected Long rewards;// bigint(20)
	protected Integer size;// int(11)
	protected String prevHash;// char(64)
	protected Integer bits;// int(10) unsigned
	protected Integer version;// int(11)
//	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	protected Date createdAt;// datetime
	protected Integer confirmedNum;// int(10)
	protected String intervalTime;//爆块间隔

	public String getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(String intervalTime) {
		this.intervalTime = intervalTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getPuid() {
		return puid;
	}

	public void setPuid(Long puid) {
		this.puid = puid;
	}

	public Long getWorkerId() {
		return workerId;
	}

	public void setWorkerId(Long workerId) {
		this.workerId = workerId;
	}

	public String getWorkerFullName() {
		return workerFullName;
	}

	public void setWorkerFullName(String workerFullName) {
		this.workerFullName = workerFullName;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getIsOrphaned() {
		return isOrphaned;
	}

	public void setIsOrphaned(Integer isOrphaned) {
		this.isOrphaned = isOrphaned;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Long getRewards() {
		return rewards;
	}

	public void setRewards(Long rewards) {
		this.rewards = rewards;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getPrevHash() {
		return prevHash;
	}

	public void setPrevHash(String prevHash) {
		this.prevHash = prevHash;
	}

	public Integer getBits() {
		return bits;
	}

	public void setBits(Integer bits) {
		this.bits = bits;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getConfirmedNum() {
		return confirmedNum;
	}

	public void setConfirmedNum(Integer confirmedNum) {
		this.confirmedNum = confirmedNum;
	}

}
