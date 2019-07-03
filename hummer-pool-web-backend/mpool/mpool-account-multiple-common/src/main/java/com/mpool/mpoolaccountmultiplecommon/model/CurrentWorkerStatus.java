package com.mpool.mpoolaccountmultiplecommon.model;

/**
 * 矿工实时状态
 * 
 * @author chenjian2
 *
 */
public class CurrentWorkerStatus {
	private Long userId;
	private Integer count;
	private Integer workerStatus;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getWorkerStatus() {
		return workerStatus;
	}

	public void setWorkerStatus(Integer workerStatus) {
		this.workerStatus = workerStatus;
	}

	public CurrentWorkerStatus(){

	}

	public CurrentWorkerStatus(Long userId, Integer count, Integer workerStatus){
		this.userId = userId;
		this.count = count;
		this.workerStatus = workerStatus;
	}

}
