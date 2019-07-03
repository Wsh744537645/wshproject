package com.mpool.accountmultiple.service.impl;

import com.mpool.accountmultiple.mapper.BlockchainMapper;
import com.mpool.accountmultiple.service.BlockchainService;
import com.mpool.common.model.account.Blockchain;
import com.mpool.common.util.SharesConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class BlockchainServiceImpl implements BlockchainService {
	@Autowired
	private BlockchainMapper blockchainMapper;

	@Override
	public void insert(Blockchain entity) {
		blockchainMapper.insert(entity);
	}

	@Override
	public void inserts(List<Blockchain> entitys) {
		// TODO Auto-generated method stub
	}

	@Override
	public void update(Blockchain entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Serializable id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Blockchain findById(Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> getBlockchainInfo() {
		return blockchainMapper.getLimt24();
	}

	@Override
	public Map<String, Object> getBlockchainInfoSole() {
		Map<String, Object> blockchainInfoSole = blockchainMapper.getBlockchainInfoSole();
		Object hash_rate = blockchainInfoSole.get("hash_rate");
		if (hash_rate != null) {
			BigDecimal th = new BigDecimal(hash_rate.toString());
			String convertEH = SharesConvertUtil.convertEH(th);
			blockchainInfoSole.put("hash_rate", convertEH);
		}
		return blockchainInfoSole;
	}

	@Override
	public Map<String, Object> btcComBlockInfo() {
		return null;
	}

}
