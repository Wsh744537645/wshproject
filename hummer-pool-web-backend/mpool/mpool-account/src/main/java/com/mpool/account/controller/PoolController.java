package com.mpool.account.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mpool.account.mapper.pool.BlockchainAllMapper;
import com.mpool.account.tasks.ScheduledTasks;
import com.mpool.common.model.account.BlockchainAllModel;
import com.mpool.common.properties.MultipleProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.account.service.BlockchainService;
import com.mpool.account.service.PoolService;
import com.mpool.common.Result;
import com.mpool.common.util.DateUtil;
import com.mpool.common.util.page.Page;
import com.mpool.common.util.page.PageModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping(value = { "/pool", "/apis/pool" })
@Api("矿池api")
public class PoolController {

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


	@Autowired
	private BlockchainService blockchainService;
	@Autowired
	private PoolService poolService;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private BlockchainAllMapper blockchainAllMapper;
	@Autowired
	private MultipleProperties multipleProperties;

	@GetMapping("/blockchain/info/stats/24")
	@ApiOperation("最近24小时全网最难")
	@ResponseBody
	public Result getBlockchainInfo() {
		List<Map<String, Object>> blockchainInfo = blockchainService.getBlockchainInfo();
		return Result.ok(blockchainInfo);
	}

	@GetMapping("/blockchain/info/stats/1")
	@ApiOperation("最近1小时全网算力")
	@ResponseBody
	public Result getBlockchainInfoSole() {
		Map<String, Object> blockchainInfo = blockchainService.getBlockchainInfoSole();
		return Result.ok(blockchainInfo);
	}

	@GetMapping("/pool/share/30")
	@ApiOperation("获取池30天算力")
	@ResponseBody
	public Result getShare30Day() {
//		Date date = new Date();
//		date = DateUtil.addDay(date, -1);
//		String end = DateUtil.getYYYYMMdd(date);
//		date = DateUtil.addDay(date, -30);
//		String start = DateUtil.getYYYYMMdd(date);
		List<Map<String, Object>> share30;
		List<String> get30Day = get30Day(new Date());
		share30 = poolService.getShare30Day(get30Day);
		return Result.ok(share30);
	}

	public static List<String> get24Hour(Date date) {
		List<String> list = new ArrayList<>();
		for (int i = 0; i < 24; i++) {
			date = DateUtil.addHour(date, -1);
			list.add(DateUtil.getYYYYMMddHH(date));
		}
		return list;
	}

	public static List<String> get30Day(Date date) {
		List<String> list = new ArrayList<>();
		for (int i = 0; i < 30; i++) {
			date = DateUtil.addDay(date, -1);
			list.add(DateUtil.getYYYYMMdd(date));
		}
		return list;
	}

	@ApiOperation("查询爆块记录")
	@GetMapping("/found/blocks")
	public String getFoundBlocks(PageModel model, String currencyName){
		if(currencyName == null || currencyName.equals(multipleProperties.getName())){
			return "forward:/pool/found/blocksbtc";
		}
		else{

			return "forward:/mutiple/" + currencyName + "?url=pool/found/blocks";
		}
	}

	@ApiOperation("查询爆块记录")
	@GetMapping("/found/blocksbtc")
	@ResponseBody
	public Result getFoundBlocksBtc(PageModel model) {
		String[] descs = model.getDescs();
		if (descs == null) {
			descs = new String[] { "created_at" };
		}
		if (descs.length == 0) {

		}
		Page<Map<String, Object>> page = new Page<>(model);

		IPage<Map<String, Object>> map = poolService.getFoundBlocks(page);
		return Result.ok(map);
	}


	@GetMapping("/btcComBlock")
	@ApiOperation("从btc.com获取全网爆块数")
	@ResponseBody
	public Result getBtcComBlock() {

		RestTemplate restTemplate = new RestTemplate();
		String day = DateUtil.getYYYYMMdd(DateUtil.addDay(new Date(), -1));
		ResponseEntity<String> forEntity = restTemplate.getForEntity("https://chain.api.btc.com/v3/block/date/" + day,
				String.class);
		log.debug("{}", forEntity);
		log.info("The time is now {}", dateFormat.format(new Date()));
		Gson gson = new Gson();
		JsonObject fromJson = gson.fromJson(forEntity.getBody(), JsonObject.class);
		JsonArray asJsonArray = fromJson.getAsJsonArray("data");
//		System.out.println(asJsonArray);
//		System.out.println("block size = " + asJsonArray.size());
//		System.out.println(forEntity.getBody());
		int blockNumByDay = asJsonArray.size();
		BlockchainAllModel blockchainAllModel = new BlockchainAllModel();
		blockchainAllModel.setCreatedDay(day);
		blockchainAllModel.setBlocks(blockNumByDay);
		blockchainAllMapper.insert(blockchainAllModel);
			return Result.ok(blockNumByDay);
	}


//	public static void main(String[] args) {
//		RestTemplate restTemplate = new RestTemplate();
//		String day = DateUtil.getYYYYMMdd(DateUtil.addDay(new Date(), -1));
//		ResponseEntity<String> forEntity = restTemplate.getForEntity("https://chain.api.btc.com/v3/block/date/" + day,
//				String.class);
//		log.debug("{}", forEntity);
//		log.info("The time is now {}", dateFormat.format(new Date()));
//		Gson gson = new Gson();
//		JsonObject fromJson = gson.fromJson(forEntity.getBody(), JsonObject.class);
//		JsonArray asJsonArray = fromJson.getAsJsonArray("data");
//		System.out.println(asJsonArray);
//		System.out.println("block size = " + asJsonArray.size());
//		System.out.println(forEntity.getBody());
//	}
}
