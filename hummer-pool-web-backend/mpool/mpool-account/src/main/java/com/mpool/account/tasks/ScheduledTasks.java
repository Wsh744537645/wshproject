package com.mpool.account.tasks;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mpool.account.mapper.BlockchainMapper;
import com.mpool.account.mapper.pool.BlockchainAllMapper;
import com.mpool.account.mapper.pool.StatsPoolDayMapper;
import com.mpool.common.model.account.BlockchainAllModel;
import com.mpool.common.model.pool.StatsPoolDay;
import com.mpool.common.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.mpool.common.model.account.Blockchain;
import com.mpool.common.util.UUIDUtil;

@Component
public class ScheduledTasks {
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private BlockchainMapper blockchainMapper;
	@Autowired
	private BlockchainAllMapper blockchainAllMapper;
	@Autowired
	private StatsPoolDayMapper statsPoolDayMapper;
	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


	//5秒钟执行一次 测试
//	@Scheduled(cron = "*/5 * * * * ?")
	//一小时获取一次全网算力
	@Scheduled(cron = "0 0 */1 * * ?")
	public void reportCurrentTime() {

		int hour = UUIDUtil.generateYYMMDDHH();
		String day = UUIDUtil.generateyyyyMMdd();
		Blockchain blockchain = blockchainMapper.selectById(hour);
		if (blockchain == null) {
			ResponseEntity<Blockchain> forEntity = restTemplate.getForEntity("https://api.blockchain.info/stats",
					Blockchain.class);
			log.debug("{}", forEntity);
			log.info("The time is now {}", dateFormat.format(new Date()));
			forEntity.getBody().setHour(hour);
			forEntity.getBody().setDay(day);
			blockchainMapper.insert(forEntity.getBody());

		}
	}

	/**
	 * 	从btc.com上获取每天全网爆块数
	 */
	// @Scheduled(cron = "0 0 */1 * * ?")// 一小时获取一次
//	@Scheduled(cron = "1 0 0 * * ?")一天执行一次
//	@Scheduled(cron = "*/5 * * * * ?")//每5秒执行一次
//	@Scheduled(cron = "0 0 3 * * ?")//每天凌晨三点
	@Scheduled(cron = "1 0 0 * * ?")//每天早上八点01秒执行
	public void getBtcComBlock() {

		RestTemplate restTemplate = new RestTemplate();
		String day = DateUtil.getYYYYMMdd(DateUtil.addDay(new Date(), -1));
		BlockchainAllModel blockchainAllModel = new BlockchainAllModel();
		blockchainAllModel.setCreatedDay(day);
		try {
			ResponseEntity<String> forEntity = restTemplate
					.getForEntity("https://chain.api.btc.com/v3/block/date/" + day, String.class);
			log.debug("{}", forEntity);
			log.info("The time is now {}", dateFormat.format(new Date()));
			Gson gson = new Gson();
			JsonObject fromJson = gson.fromJson(forEntity.getBody(), JsonObject.class);
			JsonArray asJsonArray = fromJson.getAsJsonArray("data");
			int blockNumByDay = asJsonArray.size();
			blockchainAllModel.setBlocks(blockNumByDay);
		} catch (Exception e) {
			log.error("", e);
			blockchainAllModel.setBlocks(0);
		}
//获取我们矿池过去一天算力，和获取过去一天全网算力
		Double hashRate = blockchainMapper.getDayHashRateAVG(day);
		blockchainAllModel.setHashRate(hashRate);
		StatsPoolDay poolDay = statsPoolDayMapper.selectById(day);
		if(poolDay != null) {
			blockchainAllModel.setPoolHashRate(poolDay.getHashAcceptT());
		}
		blockchainAllMapper.insert(blockchainAllModel);
	}



}
