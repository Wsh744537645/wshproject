package com.mpool.mpoolaccountmultiplecommon.model.subaccount;

public class SubAccountList {

	private Long userId;

	private String username;
	/**
	 * 最低打款金额
	 */
	private Long miniPay;
	/**
	 * 结算方式
	 */
	private String payType;

	/**
	 * 钱包地址
	 */
	private String walletAddr;
	/**
	 * 币种BTC
	 */
	private String walletType;
	/**
	 * 现在算力
	 */
	private double currentShareT;

	/**
	 * 5分钟
	 */
	private double hashAccept5mT;
	/**
	 * 总数
	 */
	private int workerTotal;
	/**
	 * 活跃数
	 */
	private int workerActive;

	public Long getMiniPay() {
		return miniPay;
	}

	public void setMiniPay(Long miniPay) {
		this.miniPay = miniPay;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
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

	public String getWalletAddr() {
		return walletAddr;
	}

	public void setWalletAddr(String walletAddr) {
		this.walletAddr = walletAddr;
	}

	public String getWalletType() {
		return walletType;
	}

	public void setWalletType(String walletType) {
		this.walletType = walletType;
	}

	public double getCurrentShareT() {
		return currentShareT;
	}

	public void setCurrentShareT(double currentShareT) {
		this.currentShareT = currentShareT;
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

	public double getHashAccept5mT() {
		return hashAccept5mT;
	}

	public void setHashAccept5mT(double hashAccept5mT) {
		this.hashAccept5mT = hashAccept5mT;
	}

}
