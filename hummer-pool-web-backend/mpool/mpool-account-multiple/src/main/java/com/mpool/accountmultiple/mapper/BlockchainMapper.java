package com.mpool.accountmultiple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.Blockchain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface BlockchainMapper extends BaseMapper<Blockchain> {
	@Select("SELECT\r\n" + "	blocks_size,\r\n" + "	difficulty,\r\n" + "	estimated_btc_sent,\r\n"
			+ "	estimated_transaction_volume_usd,\r\n" + "	hash_rate,\r\n" + "	market_price_usd,\r\n"
			+ "	miners_revenue_btc,\r\n" + "	miners_revenue_usd,\r\n" + "	minutes_between_blocks,\r\n"
			+ "	n_blocks_mined,\r\n" + "	n_blocks_total,\r\n" + "	n_btc_mined,\r\n" + "	n_tx,\r\n"
			+ "	nextretarget,\r\n" + "	`timestamp`,\r\n" + "	total_btc_sent,\r\n" + "	total_fees_btc,\r\n"
			+ "	totalbc,\r\n" + "	trade_volume_btc,\r\n" + "	trade_volume_usd\r\n" + "FROM\r\n"
			+ "	account_blockchain\r\n" + "ORDER BY\r\n" + "	`hour` DESC\r\n" + "LIMIT 24")
	List<Map<String, Object>> getLimt24();

	@Select("SELECT\r\n" + "	blocks_size,\r\n" + "	difficulty,\r\n" + "	estimated_btc_sent,\r\n"
			+ "	estimated_transaction_volume_usd,\r\n" + "	hash_rate,\r\n" + "	market_price_usd,\r\n"
			+ "	miners_revenue_btc,\r\n" + "	miners_revenue_usd,\r\n" + "	minutes_between_blocks,\r\n"
			+ "	n_blocks_mined,\r\n" + "	n_blocks_total,\r\n" + "	n_btc_mined,\r\n" + "	n_tx,\r\n"
			+ "	nextretarget,\r\n" + "	`timestamp`,\r\n" + "	total_btc_sent,\r\n" + "	total_fees_btc,\r\n"
			+ "	totalbc,\r\n" + "	trade_volume_btc,\r\n" + "	trade_volume_usd\r\n" + "FROM\r\n"
			+ "	account_blockchain\r\n" + "ORDER BY\r\n" + "	`hour` DESC\r\n" + "LIMIT 1")
	Map<String, Object> getBlockchainInfoSole();

	@Select("SELECT AVG(hash_rate) FROM account_blockchain WHERE `day` = #{day}")
	Double getDayHashRateAVG(@Param("day") String day);
}
