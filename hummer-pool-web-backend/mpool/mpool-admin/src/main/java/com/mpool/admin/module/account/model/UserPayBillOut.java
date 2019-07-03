package com.mpool.admin.module.account.model;

import java.util.Date;

public class UserPayBillOut {

	/**
	 * 用户Id
	 */
	private Long userId;
	/**
	 * 子账号名称
	 */
	private String username;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 费率
	 */
	private double fppsRate;
	/**
	 * 昨日算力
	 */
	private double pastDayShare;
	/**
	 * 余额
	 */
	private long totalDue;
	/**
	 * 已支付
	 */
	private long totalPaid;
	/**
	 * 昨日收益
	 */
	private long yesterday;

	/**
	 * 实时的
	 */
	double currentShare;
	/**
	 * 支付中的
	 */
	private long inPayment;

	private String lastIp;
	/**
	 * 最后登录时间
	 */
	private Date lastTime;

	/**
	 * 24小时
	 */
	private double share24T;

	/**
	 * 30天
	 */
	private double share30T;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public double getFppsRate() {
		return fppsRate;
	}

	public void setFppsRate(double fppsRate) {
		this.fppsRate = fppsRate;
	}

	public double getPastDayShare() {
		return pastDayShare;
	}

	public void setPastDayShare(double pastDayShare) {
		this.pastDayShare = pastDayShare;
	}

	public long getTotalDue() {
		return totalDue + inPayment;
	}

	public void setTotalDue(long totalDue) {
		this.totalDue = totalDue;
	}

	public long getTotalPaid() {
		return totalPaid;
	}

	public void setTotalPaid(long totalPaid) {
		this.totalPaid = totalPaid;
	}

	public long getYesterday() {
		return yesterday;
	}

	public void setYesterday(long yesterday) {
		this.yesterday = yesterday;
	}

	public long getTotalRevenue() {
		return totalPaid + totalDue + inPayment;
	}

	public double getCurrentShare() {
		return currentShare;
	}

	public void setCurrentShare(double currentShare) {
		this.currentShare = currentShare;
	}

	public long getInPayment() {
		return inPayment;
	}

	public void setInPayment(long inPayment) {
		this.inPayment = inPayment;
	}

	public String getLastIp() {
		return lastIp;
	}

	public void setLastIp(String lastIp) {
		this.lastIp = lastIp;
	}

	public Date getLastTime() {
//		if (getLastTime()==null){
//			return null;
//		}
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public double getShare24T() {
		return share24T;
	}

	public void setShare24T(double share24t) {
		share24T = share24t;
	}

	public double getShare30T() {
		return share30T;
	}

	public void setShare30T(double share30t) {
		share30T = share30t;
	}

}
