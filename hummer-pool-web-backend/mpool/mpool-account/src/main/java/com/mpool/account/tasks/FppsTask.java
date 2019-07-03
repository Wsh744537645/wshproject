package com.mpool.account.tasks;

import com.mpool.account.service.bill.UserPayService;
import com.mpool.account.service.fpps.FppsRatioDayService;
import com.mpool.account.service.fpps.SettlementService;
import com.mpool.account.service.fpps.UserRateDayService;
import com.mpool.common.model.account.fpps.FppsRatioDay;
import com.mpool.common.model.account.fpps.UserRateDay;
import com.mpool.common.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FppsTask {

	private static final Logger log = LoggerFactory.getLogger(FppsTask.class);

	@Autowired
	private UserPayService userPayService;
	@Autowired
	private FppsRatioDayService fppsRatioDayService;
	@Autowired
	private UserRateDayService userRateDayService;
	@Autowired
	private SettlementService settlementService;

	/**
	 * 每天8点自动 计费,uct时间，时差8小时
	 */
	@Scheduled(cron = "0 25 0 * * ?")
	public void taskBill() {
		userPayService.taskBill();
	}

	/**
	 * 0点前5min读取block_reward表中height最大的432个（没这么多数据则取有多少取多少）
	 * 数据reward_block+reward_fees除以reward_block，算得平均值，
	 * 作为计算0点后这个当天已获取收益的系数（应该是1.05以内的数字，偏差太大看是不是算错了） 并存入数据库
	 */
	@Scheduled(cron = "0 55 23 * * ?")
	public void taskFppsRatio() {
		fppsRatioDayService.insert(new FppsRatioDay());
	}

	/**
	 * 计算用户当天收益首先于0点将所有fpps的用户的费率存入内存hashmap中（当天更改的费率不生效，以存入数据库的费率为准）
	 * 
	 * 费率
	 */
	@Scheduled(cron = "1 0 0 * * ?")
	public void taskUserFppsRate() {
		log.debug("begin taskUserFppsRate ------------------------------------------->");
		String yyyymMdd = DateUtil.getYYYYMMdd(new Date());
		int day = Integer.parseInt(yyyymMdd);
		Map<Long, Integer> userFppsRate = new HashMap<>();
		List<UserRateDay> list = userRateDayService.inserts(day);
		for (UserRateDay userRateDay : list) {
			userFppsRate.put(userRateDay.getPuid(), userRateDay.getFppsRate());
		}
		log.debug("userFppsRate => {}", userFppsRate);
		settlementService.setUserFppsRate(userFppsRate);
		log.debug("end taskUserFppsRate ------------------------------------------->");
	}

	/**
	 * 计算用户当天收益首先于0点将所有fpps的用户的费率存入内存hashmap中（当天更改的费率不生效，以存入数据库的费率为准）
	 */

	//5秒钟执行一次 测试使用
//	@Scheduled(cron = "*/5 * * * * ?")
	@Scheduled(cron = "0 20 0 * * ?")
	public void taskSettlement() {
		Date date = DateUtil.addDay(new Date(), -1);
		String yyyymMdd = DateUtil.getYYYYMMdd(date);
		int day = Integer.parseInt(yyyymMdd);
		settlementService.settlement(day);

	}

}
