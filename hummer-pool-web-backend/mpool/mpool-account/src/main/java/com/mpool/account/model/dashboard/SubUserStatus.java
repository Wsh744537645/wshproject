package com.mpool.account.model.dashboard;

public class SubUserStatus {
	private Long userId;
	private String username;

	/**
	 * 5分钟
	 */
	private double share5m;

	/**
	 * 15 分钟
	 */
	private double share15m;

	/**
	 * 24 小时
	 */
	private double share24h;

	/**
	 * 总共矿机 (三种状态矿机 小程序展示)
	 */
	private int workerTotal;

	/**
	 * 非活跃 已改成失效
	 */
	private int workerInactive;

	/**
	 * 活跃
	 */
	private int workerActive;

	/**
	 * 离线 已改成不活跃
	 */
	private int offLine;

	/**
	 * 已支付
	 */
	private long totalPaid;

	/**
	 * 今日预收
	 */
	private long today;

	/**
	 * 余额
	 */
	private long totalDue;

	/**
	 * 昨日支付
	 */
	private long yesterday;

	/**
	 * 当前矿机在线率（活跃数量/全部矿机数量） --微信小程序展示字段
	 */
	private Double onlineRate;

	public Double getOnlineRate() {
		return onlineRate;
	}

	public void setOnlineRate(Double onlineRate) {
		this.onlineRate = onlineRate;
	}

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

	public double getShare5m() {
		return share5m;
	}

	public void setShare5m(double share5m) {
		this.share5m = share5m;
	}

	public double getShare15m() {
		return share15m;
	}

	public void setShare15m(double share15m) {
		this.share15m = share15m;
	}

	public double getShare24h() {
		return share24h;
	}

	public void setShare24h(double share24h) {
		this.share24h = share24h;
	}

	public int getWorkerTotal() {
		return workerTotal;
	}

	public void setWorkerTotal(int workerTotal) {
		this.workerTotal = workerTotal;
	}

	public int getWorkerActive() {
		return workerActive;
	}

	public void setWorkerActive(int workerActive) {
		this.workerActive = workerActive;
	}

	public long getTotalPaid() {
		return totalPaid;
	}

	public void setTotalPaid(long totalPaid) {
		this.totalPaid = totalPaid;
	}

	public long getToday() {
		return today;
	}

	public void setToday(long today) {
		this.today = today;
	}

	public long getTotalDue() {
		return totalDue;
	}

	public void setTotalDue(long totalDue) {
		this.totalDue = totalDue;
	}

	public long getYesterday() {
		return yesterday;
	}

	public void setYesterday(long yesterday) {
		this.yesterday = yesterday;
	}

	public int getWorkerInactive() {
		return workerInactive;
	}

	public void setWorkerInactive(int workerInactive) {
		this.workerInactive = workerInactive;
	}

	public int getOffLine() {
		return offLine;
	}

	public void setOffLine(int offLine) {
		this.offLine = offLine;
	}

}
