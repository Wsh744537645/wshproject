package com.mpool.admin.module.dashboard.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mpool.admin.InModel.InBlock;
import com.mpool.admin.InModel.InBlockchain;
import com.mpool.admin.mapperUtils.account.BlockchainAllMapperUtils;
import com.mpool.admin.mapperUtils.account.FoundBlocksMapperUtils;
import com.mpool.admin.mapperUtils.pool.MiningWorkersMapperUtils;
import com.mpool.admin.mapperUtils.pool.StatsPoolDayMapperUtils;
import com.mpool.admin.mapperUtils.pool.StatsPoolHourMapperUtils;
import com.mpool.admin.mapperUtils.pool.StatsWorkersDayMapperUtils;
import com.mpool.admin.module.account.mapper.BlockchainAllMapper;
import com.mpool.admin.module.account.mapper.BlockchainMapper;
import com.mpool.admin.module.dashboard.service.DashboardService;
import com.mpool.admin.module.pool.mapper.MiningWorkersMapper;
import com.mpool.admin.module.pool.mapper.StatsPoolDayMapper;
import com.mpool.admin.module.pool.mapper.StatsPoolHourMapper;
import com.mpool.admin.module.pool.mapper.StatsWorkersDayMapper;
import com.mpool.common.model.account.BlockchainAllModel;
import com.mpool.common.model.account.FoundBlocks;
import com.mpool.common.model.pool.MiningWorkers;
import com.mpool.common.model.pool.StatsPoolDay;
import com.mpool.common.model.pool.StatsPoolHour;
import com.mpool.common.util.BTCUtil;
import com.mpool.common.util.DateUtil;
import com.mpool.common.util.page.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class DashboardServiceImpl implements DashboardService {

	private static final Logger log = LoggerFactory.getLogger(DashboardServiceImpl.class);
	@Autowired
	private StatsPoolHourMapperUtils statsPoolHourMapper;
	@Autowired
	private MiningWorkersMapperUtils miningWorkersMapper;
	@Autowired
	private StatsPoolDayMapperUtils statsPoolDayMapper;
	@Autowired
	private StatsWorkersDayMapperUtils statsWorkersDayMapper;
	@Autowired
	private FoundBlocksMapperUtils foundBlocksMapperUtils;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private BlockchainMapper blockchainMapper;
	@Autowired
	private BlockchainAllMapper blockchainAllMapper;
	@Autowired
	private BlockchainAllMapperUtils blockchainAllMapperUtils;
	@Autowired
	private RedisTemplate redisTemplate = null;

	@Override
	public Map<String, Object> currentPoolInfo() {
		// MiningWorkers miningWorkers =
		// miningWorkersMapper.getMiningWorkersByPool();
		MiningWorkers miningWorkers = new MiningWorkers();
		Map<String, Object> map = new HashMap<>();
		Date date = new Date();
		try {
			// 矿池数据库连接
			ResponseEntity<String> forEntity = restTemplate.getForEntity("http://39.97.175.120:9020/", String.class);
			Gson gson = new Gson();
			JsonObject fromJson = gson.fromJson(forEntity.getBody(), JsonObject.class);

			JsonObject pool = fromJson.getAsJsonObject("data").getAsJsonObject("pool");
			long accept5m = pool.getAsJsonArray("accept").get(1).getAsLong();
			long accept15m = pool.getAsJsonArray("accept").get(2).getAsLong();
			long accept1h = pool.getAsJsonArray("accept").get(3).getAsLong();
			miningWorkers.setAccept1h(accept1h);
			miningWorkers.setAccept5m(accept5m);
			miningWorkers.setAccept15m(accept15m);
		} catch (Exception e) {
			log.error("get pool http://39.97.175.120:9020/ error ", e);
		}
		// 实时数据（5分钟和15分钟一样）
		Double currentShare = miningWorkers.getHashAccept15mT();
		// 统计全部活跃的矿机数
		Integer activeCount = miningWorkersMapper.getPoolWorkerActiveCount();
		// 以下是获取24小时算力（从stats_pool_hour表统计creat_at字段最近24小时的share_accept值累计后得出平均值除以24H）
		List<String> hours = DateUtil.getPast24Hour();
		List<StatsPoolHour> statsPoolHours = statsPoolHourMapper.getStatsPoolHourByHours(hours);
		Double accept24h = statsPoolHours.stream().map(StatsPoolHour::getHashAcceptT).reduce(0d, (a, b) -> a + b);
//        获取历史爆块总数
		Integer block = foundBlocksMapperUtils.getBlock();
		// 获取历史总爆块收益
		Long revenue = foundBlocksMapperUtils.getRevenue();
//        获取今日爆块数
		Integer nowBlock = foundBlocksMapperUtils.getNowBlock(date);
		// 今日爆块收益
		Long nowRevenue = foundBlocksMapperUtils.getNowRevenue(date);
		map.put("accept5m", miningWorkers.getHashAccept15mT());
		map.put("accept1h", miningWorkers.getHashAccept1hT());
		// share累计平均值除以24小时
		map.put("accept24h", accept24h / 24);
		map.put("currentShare", currentShare);
		map.put("activeCount", activeCount);
		map.put("revenue", revenue);
		map.put("block", block);
		map.put("nowRevenue", nowRevenue);
		map.put("nowBlock", nowBlock);
		return map;
	}

	@Override
	public List<StatsPoolDay> getPoolPast30DayShare() {
		List<String> days = DateUtil.getPast30Days();
		List<StatsPoolDay> list = statsPoolDayMapper.getPoolPast30DayShare(days);
		if (list.size() != days.size()) {
			for (StatsPoolDay statsPoolDay : list) {
				days.remove(statsPoolDay.getDay().toString());
			}
			for (String day : days) {
				StatsPoolDay statsPoolDay = new StatsPoolDay();
				statsPoolDay.setDay(Integer.parseInt(day));
				list.add(statsPoolDay);
			}
		}
		return list;
	}

	@Override
	public List<StatsPoolDay> getPoolPast30DayShareCache() {
		String key = "poolPast30DayShare_" + DateUtil.getYYYYMMdd(new Date());
		List<StatsPoolDay>  list = (List<StatsPoolDay>)redisTemplate.opsForValue().get(key);
		if(list == null){
			list = getPoolPast30DayShare();
			redisTemplate.opsForValue().set(key, list, 1, TimeUnit.DAYS);
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getPoolPast30DayWorker() {
		List<String> days = DateUtil.getPast30Days();
		List<Map<String, Object>> list = statsWorkersDayMapper.getPoolPast30DayWorker(days);
		if (list.size() != days.size()) {
			for (Map<String, Object> map : list) {
				days.remove(map.get("day").toString());
			}
			for (String day : days) {
				Map<String, Object> map = new HashMap<>();
				map.put("day", Integer.parseInt(day));
				map.put("count", 0);
				list.add(map);
			}
		}

		return list;
	}

	@Override
	public List<Map<String, Object>> getPoolPast30DayWorkerCache() {
		String key = "poolPast30DayWorker_" + DateUtil.getYYYYMMdd(new Date());
		List<Map<String, Object>> list = (List<Map<String, Object>>)redisTemplate.opsForValue().get(key);
		if(list == null){
			list = getPoolPast30DayWorker();
			redisTemplate.opsForValue().set(key, list, 1, TimeUnit.DAYS);
		}
		return list;
	}

	@Override
	public List<FoundBlocks> getHistoryBlock() {
		List<FoundBlocks> list = foundBlocksMapperUtils.getHistoryBlock();
		List<FoundBlocks> lis = new ArrayList<>();
		//获取每次爆块的间隔时间并返回
		for (int i = 0; i <= list.size(); i++) {
			for (FoundBlocks blocks : list) {
				FoundBlocks block1 = list.get(i);
				if (i<list.size()-1){//第一次爆块的记录不计算间隔时间
				FoundBlocks block2 = list.get(++i);
				Date date1 = block1.getCreatedAt();
				Date date2 = block2.getCreatedAt();
				//获取间隔毫秒数
				long between = date1.getTime() - date2.getTime();
				//计算间隔时分秒
				String hms = betweenTime(between);
				blocks.setIntervalTime(hms);
				lis.add(blocks);
			}else{
					FoundBlocks block3 = list.get(i);
					lis.add(block3);
					return lis;
				}
		}
	}
		return lis;
	}
	//计算时分秒
	private String betweenTime(Long ms) {

		Integer ss = 1000;
		Integer mi = ss * 60;
		Integer hh = mi * 60;
		Integer dd = hh * 24;

		Long day = ms / dd;
		Long hour = (ms - day * dd) / hh;
		Long minute = (ms - day * dd - hour * hh) / mi;
		Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
		Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

		StringBuffer sb = new StringBuffer();
		if(day > 0) {
			sb.append(day+"天");
		}
		if(hour > 0) {
			sb.append(hour+"小时");
		}
		if(minute > 0) {
			sb.append(minute+"分");
		}
		if(second > 0) {
			sb.append(second+"秒");
		}
		if(milliSecond > 0) {
			sb.append(milliSecond+"毫秒");
		}
		return sb.toString();

//		long day = between / (24 * 60 * 60 * 1000);
//		long hour = (between / (60 * 60 * 1000) - day * 24);
//		long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
//		long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
//		return day + "天" + hour + "小时" + min + "分" + s + "秒";
	}

	@Override
	public List<StatsPoolHour> getPoolPast24HShare() {
		List<String> hours = DateUtil.getPast24Hour();
		List<StatsPoolHour> statsPoolHours = statsPoolHourMapper.getStatsPoolHourByHours(hours);
		if (statsPoolHours.size() != hours.size()) {
			for (StatsPoolHour statsPoolHour : statsPoolHours) {
				String hourStr = statsPoolHour.getHour() + "";
				if (hours.contains(hourStr)) {
					hours.remove(hourStr);
				}
			}
			for (String hour : hours) {
				StatsPoolHour statsPoolHour = new StatsPoolHour();
				statsPoolHour.setHour(Integer.parseInt(hour));
				statsPoolHours.add(statsPoolHour);
			}
		}
		return statsPoolHours;
	}

	@Override
	public IPage<FoundBlocks> getHistoryBlockList(InBlock date, IPage<FoundBlocks> page) {

		log.debug("page => {}", page);
		Date str = date.getStrTime();
		Date end = date.getEndTime();
		if (end != null) {
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(end);
			rightNow.add(Calendar.HOUR, 23);
			rightNow.add(Calendar.MINUTE, 59);
			rightNow.add(Calendar.SECOND, 59);
			end = rightNow.getTime();
		}
		log.debug("getHistoryBlockList strTime =>{},endTime =>{}", str, end);

		IPage<FoundBlocks> pageList = foundBlocksMapperUtils.getHistoryBlockList(page, str, end);
		return pageList;

	}

	@Override
	public List<List<Object>> exportHistoryBlockList(InBlock date) {
		Date strTime = date.getStrTime();
		Date endTime = date.getEndTime();
		if (endTime != null) {
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(endTime);
			rightNow.add(Calendar.HOUR, 23);
			rightNow.add(Calendar.MINUTE, 59);
			rightNow.add(Calendar.SECOND, 59);
			endTime = rightNow.getTime();
		}
		// 从数据库获取按时间段搜索出来的爆块记录；
		IPage<FoundBlocks> page = new Page<>();
		page.setSize(Integer.MAX_VALUE);
		IPage<FoundBlocks> historyBlockList = foundBlocksMapperUtils.getHistoryBlockList(page, strTime, endTime);
		List<FoundBlocks> foundBlocks = historyBlockList.getRecords();
		List<List<Object>> list = new ArrayList<>();
		for (FoundBlocks blocks : foundBlocks) {
			List<Object> objects = new ArrayList<>();
			// 块高度
			objects.add(blocks.getHeight());
			// 块时间
			objects.add(blocks.getCreatedAt());
			// Hash
			objects.add(blocks.getHash());
			// 获取确认个数
			objects.add(blocks.getConfirmedNum());
			// 获取奖励
			objects.add(BTCUtil.formatBTCNONull(blocks.getRewards()));
			// 体积
			objects.add(blocks.getSize());
			list.add(objects);
		}
		return list;
	}

	// 全网算力和全网爆块数和矿池算力
	@Override
	public IPage<InBlockchain> getBlockchainInfo(IPage<InBlockchain> page, InBlock date) {

//		Date str = date.getStrTime();
//		Date end = date.getEndTime();
//		List<String> days = new ArrayList<>();
//
//		if (str != null && end != null) {
//			List<String> dayss = new ArrayList<>();
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//			String sTime = sdf.format(str); // 20190215
//			String eTime = sdf.format(end); // 20190220
//			int s = Integer.parseInt(sTime);
//			int e = Integer.parseInt(eTime);
//			for (int i = s; i <= e; i++) {
//				String d = i + "";
//				dayss.add(d);
//			}
//			days.addAll(dayss);
//		} else {
//			List<String> dayss = DateUtil.getPast20Days();
//			days.addAll(dayss);
//		}

//		Map<String, InBlockchain> outBlockchainMap = new HashMap<>();
//		List<String> days = new ArrayList<>();
////		 获取我们矿池当前24小时算力
//
//		List<InBlockchain> past1DayShare = past1DaySharePage.getRecords();
//		for (InBlockchain statsPoolDay : past1DayShare) {
//			InBlockchain inBlockchain = new InBlockchain();
//			outBlockchainMap.put(statsPoolDay.getDay() + "", inBlockchain);
//			days.add(statsPoolDay.getDay() + "");
//			Long b = statsPoolDay.getShareAccept();
//			BigDecimal bigDecimal = new BigDecimal(b);
//			inBlockchain.setCurrentHashRate(bigDecimal);
//			inBlockchain.setCreatedDay(statsPoolDay.getDay() + "");
//		}
////		 获取全网算力
//		List<Blockchain> hashRate = blockchainMapper.gethashRate(days);
//		for (Blockchain blockchain : hashRate) {
//			InBlockchain inBlockchain = outBlockchainMap.get(blockchain.getDay());
//			// inBlockchain.setCreatedDay(blockchain.getDay());
//			BigDecimal hashRates = blockchain.getHashRates();
//			inBlockchain.setHashRate(hashRates);
//		}

		// 获取全网爆块数,全网算力，矿池算力
		IPage<BlockchainAllModel> blocks = blockchainAllMapperUtils.getBlocks(page, date);
		IPage<InBlockchain> past1DaySharePage = new Page<>(blocks);
		past1DaySharePage.setRecords(new ArrayList<>());
		for (BlockchainAllModel block : blocks.getRecords()) {
			InBlockchain inBlockchain = new InBlockchain();
			inBlockchain.setBlocks(block.getBlocks());
			inBlockchain.setCreatedDay(block.getCreatedDay());

			Double poolHashRate = block.getPoolHashRate();
			if (poolHashRate == null) {
				poolHashRate = 0d;
				inBlockchain.setCurrentHashRate(BigDecimal.valueOf(poolHashRate));
			} else {
				// 矿池算力单位是p 除以1000
				BigDecimal poolHashRates = BigDecimal.valueOf(poolHashRate).divide(BigDecimal.valueOf(1000), 3,
						BigDecimal.ROUND_DOWN);
				inBlockchain.setCurrentHashRate(poolHashRates);
			}

//			每天全网算力单位p除以1000000
			Double hashRate = block.getHashRate();
			if (hashRate == null) {
				hashRate = 0d;
				inBlockchain.setHashRate(BigDecimal.valueOf(hashRate));
			} else {
				BigDecimal hashRates = BigDecimal.valueOf(hashRate).divide(BigDecimal.valueOf(1000000), 3,
						BigDecimal.ROUND_DOWN);//除以10的6次方
				inBlockchain.setHashRate(hashRates);
			}

			past1DaySharePage.getRecords().add(inBlockchain);
		}
		return past1DaySharePage;
	}

}
