package com.mpool.mpoolaccountmultiplecommon.model;

public class UserWorkerStatus {
	private Long date;

	private int onLine;

	public UserWorkerStatus(Long date) {
		super();
		this.date = date;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public int getOnLine() {
		return onLine;
	}

	public void setOnLine(int onLine) {
		this.onLine = onLine;
	}

}
