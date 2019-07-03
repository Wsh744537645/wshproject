package com.mpool.common.model.account;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import static com.mpool.common.model.constant.AccountConstant.TABLE_PREFIX;

@TableName(TABLE_PREFIX + "blockchain_all")
public class BlockchainAllModel {

	// 每天全网算力
	protected Double hashRate;
	// 每天全网爆块数
	protected int blocks;
	protected String createdDay;
	// 矿池算力
	protected Double poolHashRate;


	public int getBlocks() {
		return blocks;
	}

	public void setBlocks(int blocks) {
		this.blocks = blocks;
	}

	public String getCreatedDay() {
		return createdDay;
	}

	public void setCreatedDay(String createdDay) {
		this.createdDay = createdDay;
	}

	public Double getHashRate() {
		return hashRate;
	}

	public void setHashRate(Double hashRate) {
		this.hashRate = hashRate;
	}

	public Double getPoolHashRate() {
		return poolHashRate;
	}

	public void setPoolHashRate(Double poolHashRate) {
		this.poolHashRate = poolHashRate;
	}

	//全网占比 保留三位小数点
	public Double getRate() {
//		double rate = new BigDecimal((double) this.getPoolHashRate() / this.getHashRate()).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
		DecimalFormat de = new DecimalFormat("0.000");
		Double rate = 	this.getPoolHashRate() / this.getHashRate();
		de.format(rate);
		return rate;
	}
}
