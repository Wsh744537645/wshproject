package com.mpool.common.model.account;

import static com.mpool.common.model.constant.AccountConstant.TABLE_PREFIX;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(TABLE_PREFIX + "worker")
public class Worker {

	protected Long workerId;
	protected Integer regionId = 1;
	protected String workerName;
	protected Long userId;
	protected Date lastShareTime;
	protected Integer workerStatus;
	protected Integer workerStatusStatic;
	protected Double hashAccept5mT;
	protected Double hashAccept15mT;
	protected Double hashReject15mT;
	protected Double hashAccept1hT;
	protected Double hashReject1hT;
	protected Double rateReject1h;
	protected Double rateReject15m;

	public Long getWorkerId() {
		return workerId;
	}

	public void setWorkerId(Long workerId) {
		this.workerId = workerId;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getLastShareTime() {
		return lastShareTime;
	}

	public void setLastShareTime(Date lastShareTime) {
		this.lastShareTime = lastShareTime;
	}

	public Integer getWorkerStatus() {
		updateStatus();
		return workerStatus;
	}

	public void setWorkerStatus(Integer workerStatus) {
		this.workerStatus = workerStatus;
		workerStatusStatic = workerStatus;
	}

	public Double getHashAccept5mT() {
		return hashAccept5mT;
	}

	public void setHashAccept5mT(Double hashAccept5mT) {
		this.hashAccept5mT = hashAccept5mT;
	}

	public Double getHashAccept15mT() {
		return hashAccept15mT;
	}

	public void setHashAccept15mT(Double hashAccept15mT) {
		this.hashAccept15mT = hashAccept15mT;
	}

	public Double getHashReject15mT() {
		return hashReject15mT;
	}

	public void setHashReject15mT(Double hashReject15mT) {
		this.hashReject15mT = hashReject15mT;
	}

	public Double getHashAccept1hT() {
		return hashAccept1hT;
	}

	public void setHashAccept1hT(Double hashAccept1hT) {
		this.hashAccept1hT = hashAccept1hT;
	}

	public Double getHashReject1hT() {
		return hashReject1hT;
	}

	public void setHashReject1hT(Double hashReject1hT) {
		this.hashReject1hT = hashReject1hT;
	}

	public Double getRateReject1h() {
		return rateReject1h;
	}

	public void setRateReject1h(Double rateReject1h) {
		this.rateReject1h = rateReject1h;
	}

	public Double getRateReject15m() {
		return rateReject15m;
	}

	public void setRateReject15m(Double rateReject15m) {
		this.rateReject15m = rateReject15m;
	}

	public Integer getWorkerStatusStatic() {
		return workerStatusStatic;
	}

	public String getStatus() {
		String statis = null;
		updateStatus();
		switch (this.workerStatus) {
			case 0:
				statis = "活跃";
				break;
			case 1:
				statis = "不活跃";
				break;
			case 2:
				statis = "离线";
				break;
			default:
				statis = "错误";
				break;
		}
		return statis;
	}

	protected void updateStatus(){
		if(hashAccept15mT > 0){
			workerStatus = 0;
		}else{
			if(new Date().getTime() - lastShareTime.getTime() > 24 * 60 * 60 * 1000){
				workerStatus = 2;
			}
			else
			{
				workerStatus = 1;
			}
		}
	}

	@Override
	public String toString() {
		updateStatus();
		return super.toString();
	}
}
