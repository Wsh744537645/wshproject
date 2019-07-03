package com.mpool.mpoolaccountmultiplecommon.model.export;

import com.mpool.mpoolaccountmultiplecommon.constant.Constant;

import java.util.Date;

public class PayRecord {

	private String txid;
	private Date payAt;

	private Long payBtc;

	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public Date getPayAt() {
		return payAt;
	}

	public void setPayAt(Date payAt) {
		this.payAt = payAt;
	}

	public String getPayBtc() {
		double double1 = (payBtc / Constant.MIN_PAY);
		return Double.toString(double1);
	}

	public void setPayBtc(Long payBtc) {
		this.payBtc = payBtc;
	}

}
