package com.mpool.admin.module.account.model;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mpool.admin.utils.serializer.DoubleFormatSerializer;

/**
 * 用户列表数据
 */
public class MasterUserShaerModel {
	private Long day;
	@JsonSerialize(using = DoubleFormatSerializer.class)
	private double shareAccept;
	private double shareReject;
	private double rejectRate;

	public MasterUserShaerModel(Long day) {
		super();
		this.day = day;
	}

	public Long getDay() {
		return day;
	}

	public void setDay(Long day) {
		this.day = day;
	}

	public double getShareAccept() {
		return shareAccept;
	}

	public void setShareAccept(double shareAccept) {
		this.shareAccept = this.shareAccept + shareAccept;
	}

	public double getShareReject() {
		return shareReject;
	}

	public void setShareReject(double shareReject) {
		this.shareReject = this.shareReject + shareReject;
	}

	public double getRate(){
		double sum = shareAccept+shareReject;
		if(sum == 0d){
			return 0;
		}
		return shareReject/ sum *100;
	}


	public double getRejectRate(){
		double sum = shareAccept+shareReject;
		if(sum == 0d){
			return 0;
		}
		return shareReject/ sum;
	}

}