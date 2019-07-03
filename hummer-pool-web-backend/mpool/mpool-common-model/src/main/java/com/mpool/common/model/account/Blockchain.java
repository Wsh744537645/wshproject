package com.mpool.common.model.account;

import static com.mpool.common.model.constant.AccountConstant.TABLE_PREFIX;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(TABLE_PREFIX + "blockchain")
public class Blockchain {
	@TableId(type = IdType.INPUT)
	protected Integer hour;
	protected String day;

	protected BigDecimal timestamp;
	protected BigDecimal market_price_usd;
	protected BigDecimal hash_rate;
	@TableField(exist = false)
	protected BigDecimal hashRates;
	protected Long total_fees_btc;
	protected Long n_btc_mined;
	protected Long n_tx;
	protected Long n_blocks_mined;
	protected BigDecimal minutes_between_blocks;
	protected Long totalbc;
	protected Long n_blocks_total;
	protected BigDecimal estimated_transaction_volume_usd;
	protected Long blocks_size;
	protected BigDecimal miners_revenue_usd;
	protected Long nextretarget;
	protected Long difficulty;
	protected Long estimated_btc_sent;
	protected Long miners_revenue_btc;
	protected Long total_btc_sent;
	protected BigDecimal trade_volume_btc;
	protected BigDecimal trade_volume_usd;

	public BigDecimal getHashRates() {
		return hashRates;
	}

	public void setHashRates(BigDecimal hashRates) {
		this.hashRates = hashRates;
	}

	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public BigDecimal getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(BigDecimal timestamp) {
		this.timestamp = timestamp;
	}

	public BigDecimal getMarket_price_usd() {
		return market_price_usd;
	}

	public void setMarket_price_usd(BigDecimal market_price_usd) {
		this.market_price_usd = market_price_usd;
	}

	public BigDecimal getHash_rate() {
		return hash_rate;
	}

	public void setHash_rate(BigDecimal hash_rate) {
		this.hash_rate = hash_rate;
	}

	public Long getTotal_fees_btc() {
		return total_fees_btc;
	}

	public void setTotal_fees_btc(Long total_fees_btc) {
		this.total_fees_btc = total_fees_btc;
	}

	public Long getN_btc_mined() {
		return n_btc_mined;
	}

	public void setN_btc_mined(Long n_btc_mined) {
		this.n_btc_mined = n_btc_mined;
	}

	public Long getN_tx() {
		return n_tx;
	}

	public void setN_tx(Long n_tx) {
		this.n_tx = n_tx;
	}

	public Long getN_blocks_mined() {
		return n_blocks_mined;
	}

	public void setN_blocks_mined(Long n_blocks_mined) {
		this.n_blocks_mined = n_blocks_mined;
	}

	public BigDecimal getMinutes_between_blocks() {
		return minutes_between_blocks;
	}

	public void setMinutes_between_blocks(BigDecimal minutes_between_blocks) {
		this.minutes_between_blocks = minutes_between_blocks;
	}

	public Long getTotalbc() {
		return totalbc;
	}

	public void setTotalbc(Long totalbc) {
		this.totalbc = totalbc;
	}

	public Long getN_blocks_total() {
		return n_blocks_total;
	}

	public void setN_blocks_total(Long n_blocks_total) {
		this.n_blocks_total = n_blocks_total;
	}

	public BigDecimal getEstimated_transaction_volume_usd() {
		return estimated_transaction_volume_usd;
	}

	public void setEstimated_transaction_volume_usd(BigDecimal estimated_transaction_volume_usd) {
		this.estimated_transaction_volume_usd = estimated_transaction_volume_usd;
	}

	public Long getBlocks_size() {
		return blocks_size;
	}

	public void setBlocks_size(Long blocks_size) {
		this.blocks_size = blocks_size;
	}

	public BigDecimal getMiners_revenue_usd() {
		return miners_revenue_usd;
	}

	public void setMiners_revenue_usd(BigDecimal miners_revenue_usd) {
		this.miners_revenue_usd = miners_revenue_usd;
	}

	public Long getNextretarget() {
		return nextretarget;
	}

	public void setNextretarget(Long nextretarget) {
		this.nextretarget = nextretarget;
	}

	public Long getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Long difficulty) {
		this.difficulty = difficulty;
	}

	public Long getEstimated_btc_sent() {
		return estimated_btc_sent;
	}

	public void setEstimated_btc_sent(Long estimated_btc_sent) {
		this.estimated_btc_sent = estimated_btc_sent;
	}

	public Long getMiners_revenue_btc() {
		return miners_revenue_btc;
	}

	public void setMiners_revenue_btc(Long miners_revenue_btc) {
		this.miners_revenue_btc = miners_revenue_btc;
	}

	public Long getTotal_btc_sent() {
		return total_btc_sent;
	}

	public void setTotal_btc_sent(Long total_btc_sent) {
		this.total_btc_sent = total_btc_sent;
	}

	public BigDecimal getTrade_volume_btc() {
		return trade_volume_btc;
	}

	public void setTrade_volume_btc(BigDecimal trade_volume_btc) {
		this.trade_volume_btc = trade_volume_btc;
	}

	public BigDecimal getTrade_volume_usd() {
		return trade_volume_usd;
	}

	public void setTrade_volume_usd(BigDecimal trade_volume_usd) {
		this.trade_volume_usd = trade_volume_usd;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Blockchain [timestamp=").append(timestamp).append(", market_price_usd=")
				.append(market_price_usd).append(", hash_rate=").append(hash_rate).append(", total_fees_btc=")
				.append(total_fees_btc).append(", n_btc_mined=").append(n_btc_mined).append(", n_tx=").append(n_tx)
				.append(", n_blocks_mined=").append(n_blocks_mined).append(", minutes_between_blocks=")
				.append(minutes_between_blocks).append(", totalbc=").append(totalbc).append(", n_blocks_total=")
				.append(n_blocks_total).append(", estimated_transaction_volume_usd=")
				.append(estimated_transaction_volume_usd).append(", blocks_size=").append(blocks_size)
				.append(", miners_revenue_usd=").append(miners_revenue_usd).append(", nextretarget=")
				.append(nextretarget).append(", difficulty=").append(difficulty).append(", estimated_btc_sent=")
				.append(estimated_btc_sent).append(", miners_revenue_btc=").append(miners_revenue_btc)
				.append(", total_btc_sent=").append(total_btc_sent).append(", trade_volume_btc=")
				.append(trade_volume_btc).append(", trade_volume_usd=").append(trade_volume_usd).append("]");
		return builder.toString();
	}

}
