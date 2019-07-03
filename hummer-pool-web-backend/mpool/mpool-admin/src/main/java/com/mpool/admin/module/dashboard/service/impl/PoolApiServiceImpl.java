package com.mpool.admin.module.dashboard.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.mpool.admin.mapperUtils.dashboard.HpoolMapperUtils;
import com.mpool.admin.module.dashboard.mapper.HpoolMapper;
import com.mpool.common.model.pool.StatsUsersHour;
import com.mpool.common.model.pool.StatsWorkersHour;
import com.mpool.common.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mpool.admin.module.dashboard.service.PoolApiService;
import com.mpool.common.model.pool.MiningWorkers;

@Service
public class PoolApiServiceImpl implements PoolApiService {
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private HpoolMapperUtils hpoolMapperUtils;
	private static final Logger log = LoggerFactory.getLogger(PoolApiServiceImpl.class);

	@Override
	public MiningWorkers getCurrentShare() {

		MiningWorkers miningWorkers = new MiningWorkers();
		try {
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
		return miningWorkers;
	}

	@Override
	public List<Map<String, Object>> getUser24H(Long userId) {

		Date date = new Date();
		List<String> hours = get24Hour(date);
		String yyyymMddHH = DateUtil.getYYYYMMddHH(DateUtil.addHour(date, -25));
		hours.add(yyyymMddHH);
		List<StatsUsersHour> statsUsersHourInHour = hpoolMapperUtils.getStatsUsersHourInHour(userId, hours);
		for (String hour : hours) {
			StatsUsersHour o = new StatsUsersHour();
			o.setHour(Long.parseLong(hour));
			if (!statsUsersHourInHour.contains(o)) {
				statsUsersHourInHour.add(o);
			}
		}
		List<StatsUsersHour> collect = statsUsersHourInHour.stream()
				.sorted((a, b) -> a.getHour().compareTo(b.getHour())).collect(Collectors.toList());
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 1; i < collect.size(); i++) {
			StatsUsersHour currentItem = collect.get(i);
			Map<String, Object> map = new HashMap<>();
			double hashAcceptT = currentItem.getHashAcceptT();
			double hashRejectT = currentItem.getHashRejectT();
			map.put("baseHashAcceptT", hashAcceptT);
			map.put("baseHashRejectT", hashRejectT);
			StatsUsersHour lastItem = collect.get(i - 1);
			hashAcceptT = (hashAcceptT * 0.3873) + (lastItem.getHashAcceptT() * 0.6127);
			hashRejectT = (hashRejectT * 0.3873) + (lastItem.getHashRejectT() * 0.6127);

			double sum = hashAcceptT + hashRejectT;
			double rejectRate;
			if(sum == 0d){
					rejectRate =0d;
			}else {
				rejectRate = hashRejectT / sum;
			}

			map.put("hour", currentItem.getHour());
			map.put("hashAcceptT", hashAcceptT);
			map.put("hashRejectT", hashRejectT);
			map.put("rejectRate",rejectRate);
			list.add(map);
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getWorker24HByWorkerId(Long workerId) {
		Date date = new Date();
		List<String> hours = get24Hour(date);
		String yyyymMddHH = DateUtil.getYYYYMMddHH(DateUtil.addHour(date, -25));
		hours.add(yyyymMddHH);
		List<StatsWorkersHour> statsWorkersHourList = hpoolMapperUtils.getStatsWorkerHourByWorkerId(workerId, hours);

		List<StatsWorkersHour> collect = statsWorkersHourList.stream()
				.sorted((a, b) -> a.getHour().compareTo(b.getHour())).collect(Collectors.toList());
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 1; i < collect.size(); i++) {
			StatsWorkersHour currentItem = collect.get(i);
			Map<String, Object> map = new HashMap<>();
			double hashAcceptT = currentItem.getHashAcceptT();
			double hashRejectT = currentItem.getHashRejectT();
			map.put("baseHashAcceptT", hashAcceptT);
			map.put("baseHashRejectT", hashRejectT);
			StatsWorkersHour lastItem = collect.get(i - 1);
			hashAcceptT = (hashAcceptT * 0.3873) + (lastItem.getHashAcceptT() * 0.6127);
			hashRejectT = (hashRejectT * 0.3873) + (lastItem.getHashRejectT() * 0.6127);

			double sum = hashAcceptT + hashRejectT;
			double rejectRate;
			if(sum == 0d){
				rejectRate =0d;
			}else {
				rejectRate = hashRejectT / sum;
			}

			map.put("hour", currentItem.getHour());
			map.put("hashAcceptT", hashAcceptT);
			map.put("hashRejectT", hashRejectT);
			map.put("rejectRate",rejectRate);
			list.add(map);
		}
		return list;
	}

	private static List<String> get24Hour(Date date) {
		List<String> list = new ArrayList<>();
		for (int i = 0; i < 24; i++) {
			date = DateUtil.addHour(date, -1);
			list.add(DateUtil.getYYYYMMddHH(date));
		}
		return list;
	}

}
