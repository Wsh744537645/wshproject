package com.mpool.account.model.dashboard;

import java.math.BigDecimal;

public class UserShareChart {
	private Long date;

	private double hashAcceptT;

	private double hashRejectT;


	public UserShareChart(Long date) {
		super();
		this.date = date;
	}

	public UserShareChart() {

	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public double getHashAcceptT() {
		return hashAcceptT;
	}

	public void setHashAcceptT(double hashAcceptT) {
		this.hashAcceptT = hashAcceptT;
	}

	public double getHashRejectT() {
		return hashRejectT;
	}

	public void setHashRejectT(double hashRejectT) {
		this.hashRejectT = hashRejectT;
	}

	/**
	 * 返回拒绝率
	 * @return
	 */
	public double getRejectRate() {
		double sum = hashAcceptT+hashRejectT;
		if(sum == 0d){
			return 0;
		}
		double rate = hashRejectT/ sum *100;
		if (rate != 0d){
			BigDecimal bg = new BigDecimal(rate);
			double num1 = bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
			return num1;
		}
	return 0;
	}


}
