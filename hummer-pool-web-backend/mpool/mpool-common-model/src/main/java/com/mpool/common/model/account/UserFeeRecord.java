package com.mpool.common.model.account;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 用户实际收益记录，对应表 user_fee_record
 * 可能是机器直接产生的收益换算之后的值，或者是作为推荐人分到的收益
 */

@TableName("user_fee_record")
public class UserFeeRecord {

	protected Integer id;// ` int(32) NOT NULL AUTO_INCREMENT,
	protected Long userId;// ` bigint(20) DEFAULT NULL COMMENT '用户id',
	protected Integer day; // ` bigint(20) DEFAULT NULL COMMENT '天',
	protected Long fee;// ` bigint(20) DEFAULT NULL COMMENT '费用',
	protected Integer feeType;// ` int(2) DEFAULT NULL COMMENT '费用类型',   1：算力收益，2：推荐用户的收益，3：打币，4：奖励，5：补发，6：其他
	protected String feeDesc;// ` varchar(255) DEFAULT NULL COMMENT '费用描述',
	protected Date createTime;// ` datetime DEFAULT NULL,
	@TableField(exist = false)
	protected Long feeSum;
	// 当日总分币数
	@TableField(exist = false)
	protected Long sum;
	// 当日推荐奖励
	@TableField(exist = false)
	protected Long recommendFeeSum;

	public Long getSum() {
		return sum;
	}

	public void setSum(Long sum) {
		this.sum = sum;
	}

	public Long getRecommendFeeSum() {
		return recommendFeeSum;
	}

	public void setRecommendFeeSum(Long recommendFeeSum) {
		this.recommendFeeSum = recommendFeeSum;
	}

	public Long getFeeSum() {
		return feeSum;
	}

	public void setFeeSum(Long feeSum) {
		this.feeSum = feeSum;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Long getFee() {
		return fee;
	}

	public void setFee(Long fee) {
		this.fee = fee;
	}

	public Integer getFeeType() {
		return feeType;
	}

	public void setFeeType(Integer feeType) {
		this.feeType = feeType;
	}

	public String getFeeDesc() {
		return feeDesc;
	}

	public void setFeeDesc(String feeDesc) {
		this.feeDesc = feeDesc;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
