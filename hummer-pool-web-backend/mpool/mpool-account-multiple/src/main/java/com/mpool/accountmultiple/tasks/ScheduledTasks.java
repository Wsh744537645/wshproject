package com.mpool.accountmultiple.tasks;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mpool.accountmultiple.mapper.BlockchainMapper;
import com.mpool.accountmultiple.mapper.pool.BlockchainAllMapper;
import com.mpool.accountmultiple.mapper.pool.MiningWorkersMapper;
import com.mpool.accountmultiple.mapper.pool.StatsPoolDayMapper;
import com.mpool.common.model.account.Blockchain;
import com.mpool.common.model.account.BlockchainAllModel;
import com.mpool.common.model.pool.StatsPoolDay;
import com.mpool.common.util.DateUtil;
import com.mpool.common.util.GsonUtil;
import com.mpool.common.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ScheduledTasks {
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private BlockchainMapper blockchainMapper;
	@Autowired
	private BlockchainAllMapper blockchainAllMapper;
	@Autowired
	private StatsPoolDayMapper statsPoolDayMapper;
	@Autowired
	private MiningWorkersMapper miningWorkersMapper;

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	/**
	 * 爬取zcash数据
	 * @param url
	 * @return
	 */
	private String getZcashInfoFromNetWork(String url){
		String body = null;
		int count = 0;
		while (++count <= 5) {
			try {
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
				headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:66.0) Gecko/20100101 PostmanRuntime/7.11.0");
				headers.add("Connection", "keep-alive");
				HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

				ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
				body = GsonUtil.getGson().toJson(response.getBody());
				return body;
			} catch (Exception ex) {
				log.error("get zec all share fails. url={}", url);
			}

			try {
				Thread.sleep(2000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return body;
	}

	/**
	 * 获得request body中的key-value
	 * @param key
	 * @return
	 */
	private String getBodyJsonKeyValue(String body, String key){
		if(body == null){
			return "0";
		}

		Map<String, Object> map = GsonUtil.getGson().fromJson(body, Map.class);
		List<Map<String, Object>> dataMap = GsonUtil.getGson().fromJson(map.get("data").toString(), List.class);
		String day = UUIDUtil.generateyyyy_MM_dd();
		for (Map<String, Object> tmpMap : dataMap) {
			if (tmpMap.containsKey(day)) {
				return GsonUtil.getGson().toJson(tmpMap.get(day));
			}
		}
		log.error(">>>>>> zcash get body key value error, key not exist, body={}, key={}", body, key);
		return "0";
	}

	//5秒钟执行一次 测试
	//@Scheduled(cron = "*/5 * * * * ?")
	//一小时获取一次全网算力
	@Scheduled(cron = "0 0 */1 * * ?")
	public void reportCurrentTime() {
		int hour = UUIDUtil.generateYYMMDDHH();
		Blockchain blockchain = blockchainMapper.selectById(hour);
		if(blockchain == null) {
			String body = getZcashInfoFromNetWork("https://zcash.tokenview.com/v2api/chart/?coin=zcash&type=daily_avg_hashrate&splice=2");
			String hashRate = getBodyJsonKeyValue(body, UUIDUtil.generateyyyy_MM_dd());
			String day = UUIDUtil.generateyyyyMMdd();

			blockchain = new Blockchain();
			blockchain.setHash_rate(new BigDecimal(hashRate));
			blockchain.setHour(hour);
			blockchain.setDay(day);
			blockchainMapper.insert(blockchain);
		}
	}

	/**
	 * 	从btc.com上获取每天全网爆块数
	 */
	// @Scheduled(cron = "0 0 */1 * * ?")// 一小时获取一次
//	@Scheduled(cron = "1 0 0 * * ?")一天执行一次
	//@Scheduled(cron = "*/5 * * * * ?")//每5秒执行一次
//	@Scheduled(cron = "0 0 3 * * ?")//每天凌晨三点
	@Scheduled(cron = "1 0 0 * * ?")//每天早上八点01秒执行
	public void getBtcComBlock() {
		while (true) {
			String body = getZcashInfoFromNetWork("https://zcash.tokenview.com/v2api/chart/?coin=zcash&type=daily_block_cnt&splice=3");
			String day = DateUtil.getYYYYMMdd(DateUtil.addDay(new Date(), -1));
			BlockchainAllModel blockchainAllModel = new BlockchainAllModel();
			blockchainAllModel.setCreatedDay(day);

			String blockNum = getBodyJsonKeyValue(body, UUIDUtil.generateyyyy_MM_dd());
			blockchainAllModel.setBlocks((int) Float.parseFloat(blockNum));
			if (blockchainAllModel.getBlocks() > 0) {
				//获取我们矿池过去一天算力，和获取过去一天全网算力
				Double hashRate = blockchainMapper.getDayHashRateAVG(day);
				blockchainAllModel.setHashRate(hashRate);
				StatsPoolDay poolDay = statsPoolDayMapper.selectById(day);
				if (poolDay != null) {
					blockchainAllModel.setPoolHashRate(poolDay.getHashAcceptT());
				}
				blockchainAllMapper.insert(blockchainAllModel);
				break;
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}



}
