package com.mpool.account.service;

import java.util.List;
import java.util.Map;

import com.mpool.common.model.account.Blockchain;

public interface BlockchainService extends BaseService<Blockchain> {
	/**
	 * 获取最近24小时全最难
	 * 
	 * @return
	 */
	List<Map<String, Object>> getBlockchainInfo();

	/**
	 * 获取最新的一条
	 * @return
	 */
	Map<String, Object> getBlockchainInfoSole();

	/**
	 * wgg
	 * 获取btc.com上面的全网爆块记录
	 * @return
	 */
	Map<String, Object> btcComBlockInfo();


}
