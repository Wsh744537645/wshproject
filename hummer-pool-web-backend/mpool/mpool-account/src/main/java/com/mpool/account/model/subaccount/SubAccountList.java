package com.mpool.account.model.subaccount;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
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

	/**
	 * 账户持有的其他币种信息
	 */
	private List<Map<String, Object>> otherCurrency;


	public void addOtherCurrency(Map<String, Object> map){
		if(otherCurrency == null){
			otherCurrency = new ArrayList<>();
		}
		otherCurrency.add(map);
	}
}
