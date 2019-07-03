package com.mpool.account.service.fpps.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpool.account.mapper.RecommendUserMapper;
import com.mpool.account.mapper.bill.UserPayMapper;
import com.mpool.account.mapper.fpps.FppsRatioDayMapper;
import com.mpool.account.mapper.fpps.PoolFppsRecordMapper;
import com.mpool.account.mapper.fpps.UserFppsRecordMapper;
import com.mpool.account.mapper.pool.StatsUsersDayMapper;
import com.mpool.account.mapper.recommend.UserFeeRecordMapper;
import com.mpool.account.service.fpps.SettlementService;
import com.mpool.account.service.fpps.UserRateDayService;
import com.mpool.common.model.account.RecommendUser;
import com.mpool.common.model.account.UserFeeRecord;
import com.mpool.common.model.account.bill.UserPay;
import com.mpool.common.model.account.fpps.FppsRatioDay;
import com.mpool.common.model.account.fpps.PoolFppsRecord;
import com.mpool.common.model.account.fpps.UserFppsRecord;
import com.mpool.common.model.account.fpps.UserRateDay;
import com.mpool.common.model.pool.StatsUsersDay;
import com.mpool.common.properties.MultipleProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 结算服务
 * 
 * @author chenjian2
 *
 */
@Service
public class SettlementServiceImpl implements SettlementService {

	private static final Logger log = LoggerFactory.getLogger(SettlementServiceImpl.class);
	private final static Float fZero = Float.valueOf(0.0f);
	@Autowired
	private StatsUsersDayMapper statsUsersDayMapper;
	@Autowired
	private FppsRatioDayMapper fppsRatioDayMapper;
	@Autowired
	private PoolFppsRecordMapper poolFppsRecordMapper;
	@Autowired
	private UserFppsRecordMapper userFppsRecordMapper;
	@Autowired
	private UserRateDayService userRateDayService;
	@Autowired
	private UserPayMapper userPayMapper;
	private Map<Long, Integer> userFppsRate = new HashMap<>();
	@Autowired
	private RecommendUserMapper recommendUserMapper;
	@Autowired
	private UserFeeRecordMapper userFeeRecordMapper;
	@Autowired
	private MultipleProperties properties;

	/**
	 * 算收益
	 * 
	 * @param day
	 */
	@Override
	public void settlement(Integer day) {
		log.debug("begin settlement -------------------------------->");
		StatsUsersDay entity = new StatsUsersDay();
		entity.setDay(new Long(day));
		float ratio = getRatio(day);
		List<StatsUsersDay> list = statsUsersDayMapper.selectList(new QueryWrapper<>(entity));
		Date date = new Date();
		//总收益
		long fppsTotal = 0;
		long fppsPayUser = 0;
		List<UserFppsRecord> fppsRecords = new ArrayList<>();
		for (StatsUsersDay statsUsersDay : list) {

			Long share = statsUsersDay.getShareAccept();

			Long puid = statsUsersDay.getPuid();

			Long earn = statsUsersDay.getEarn();

			//fppsBeforeFee = earn * ratio
			Long fppsBeforeFee = BigDecimal.valueOf(earn).multiply(BigDecimal.valueOf(ratio)).longValue();

			fppsTotal += fppsBeforeFee;

			Integer fppsRate = getUserFppsRate(puid, day);

			//fppsRateD = (1000 - fppsRate) / 1000
			BigDecimal fppsRateD = BigDecimal.valueOf(1000).subtract(BigDecimal.valueOf(fppsRate))
					.divide(BigDecimal.valueOf(1000));

			//fppsAfterFee = fppsRateD * fppsBeforeFee
			Long fppsAfterFee = fppsRateD.multiply(BigDecimal.valueOf(fppsBeforeFee)).longValue();

			// fppsPayUser += fppsAfterFee;

			UserFppsRecord fppsRecord = new UserFppsRecord(puid, day, share, fppsBeforeFee, fppsAfterFee, fppsRate,
					date);

			fppsRecords.add(fppsRecord);
		}
		// 所有收益
		List<UserFeeRecord> userFeeRecordList = new ArrayList<>();

		// 算力收益 fppsRecords
		List<UserFeeRecord> shareEarnings = shareEarning(fppsRecords);
		userFeeRecordList.addAll(shareEarnings);
		// 推荐收益 fppsRecords
		List<UserFeeRecord> recommendEarning = recommendEarning(fppsRecords);
		userFeeRecordList.addAll(recommendEarning);

		// 活动收益（此处后续使用） fppsRecords
//		List<UserFeeRecord> shareEarnings = shareEarning(recommendedList,fppsRecords);
//		userFeeRecordList.addAll(shareEarnings);
		// 统计余额
		//记录当日有收益的用户
		Set<Long> userset = new HashSet<>();
		for (UserFeeRecord userFeeRecord : userFeeRecordList) {
			Long puid = userFeeRecord.getUserId();
			userset.add(puid);
			Long fee = userFeeRecord.getFee();
			fppsPayUser += fee;
			//用户当日或者几日拥有收益累加(后面的定时任务中会用到去做支付计算 bill)
			userPayMapper.additionTotalDue(fee, puid, properties.getId());
		}
		Integer fppsUserCount = userset.size();

		//计算矿机池的收益
		PoolFppsRecord poolFppsRecord = new PoolFppsRecord();
		poolFppsRecord.setCreatAt(date);
		poolFppsRecord.setDay(day);
		poolFppsRecord.setPoolId(1);
		poolFppsRecord.setFppsUserCount(fppsUserCount);
		long fppsPayPool = fppsTotal - fppsPayUser;
		poolFppsRecord.setFppsPayPool(fppsPayPool);
		poolFppsRecord.setFppsPayUser(fppsPayUser);
		poolFppsRecord.setFppsTotal(fppsTotal);
		poolFppsRecordMapper.insert(poolFppsRecord);
		if (fppsRecords.isEmpty()) {
			log.info("No revenue found");
		} else {
			userFppsRecordMapper.inserts(fppsRecords);
		}

		if (userFeeRecordList.isEmpty()) {
			log.info("user fee is empty");
		} else {
			// 插入推荐奖励记录
			userFeeRecordMapper.insets(userFeeRecordList);
		}

//换位置
//		for (UserFppsRecord userFppsRecord : fppsRecords) {
//			Long totalDue = userFppsRecord.getFppsAfterFee();
//			Long puid = userFppsRecord.getPuid();
//			userPayMapper.additionTotalDue(totalDue, puid);
//		}

		log.debug("end settlement -------------------------------->");
	}

	private float getRatio(Integer day) {

		FppsRatioDay ratio = fppsRatioDayMapper.getRatio(day);
		if (ratio == null) {
			log.warn("ratio is empt");
			Float fratio = 1.01f;
			return fratio;
		} else {
			if (ratio.getNewRatio() == null || fZero.equals(ratio.getNewRatio())) {
				Float ratios = ratio.getRatio();
				return ratios;
			} else {
				return ratio.getNewRatio();
			}
		}
	}

	private Integer getUserFppsRate(Long userId, Integer day) {
		if (this.userFppsRate.isEmpty()) {
			List<UserRateDay> findByDay = userRateDayService.findByDay(day);
			for (UserRateDay userRateDay : findByDay) {
				this.userFppsRate.put(userRateDay.getPuid(), userRateDay.getFppsRate());
			}
		}
		Integer fppsRate = userFppsRate.get(userId);
		if (fppsRate == null) {
			log.debug("userId --> {}", userId);
			UserPay userPay = userPayMapper.findByPuid(userId, properties.getId());
			if (userPay == null) {
				log.debug("pool user many puid --> {}", userId);
				return 25;
			}
			fppsRate = userPay.getFppsRate();
			UserRateDay userRateDay = new UserRateDay();
			userRateDay.setDay(day);
			userRateDay.setFppsRate(fppsRate);
			userRateDay.setPuid(userId);
			userRateDayService.insert(userRateDay);
			userFppsRate.put(userId, fppsRate);
		}
		return fppsRate;
	}

	@Override
	public void setUserFppsRate(Map<Long, Integer> map) {
		userFppsRate.clear();
		userFppsRate.putAll(map);
	}

//    算力收益
	private List<UserFeeRecord> shareEarning(List<UserFppsRecord> list) {
		List<UserFeeRecord> lis = new ArrayList<>();
		Date date = new Date();
		for (UserFppsRecord userFppsRecord : list) {
			UserFeeRecord userFeeRecord = new UserFeeRecord();
			userFeeRecord.setUserId(userFppsRecord.getPuid());
			userFeeRecord.setDay(userFppsRecord.getDay());
			userFeeRecord.setFee(userFppsRecord.getFppsAfterFee());
			userFeeRecord.setCreateTime(date);
			userFeeRecord.setFeeType(1);
			userFeeRecord.setFeeDesc("算力收益");
			lis.add(userFeeRecord);
		}
		return lis;
	}

//    推荐人奖励
	private List<UserFeeRecord> recommendEarning(List<UserFppsRecord> list) {
		// 获取所有推荐人
		List<RecommendUser> ruList = recommendUserMapper.getRecommendUserList();
		List<UserFeeRecord> lis = new ArrayList<>();
		Date date = new Date();
		Map<Long, UserFppsRecord> map = new HashMap<>();
		for (UserFppsRecord userFppsRecord : list) {
			Long puid = userFppsRecord.getPuid();
			map.put(puid, userFppsRecord);
		}
		for (RecommendUser recommendUser : ruList) {
			Long userId = recommendUser.getRecommendId();
			Long puid = recommendUser.getUserId();
			// 获取被推荐的用户收益
			UserFppsRecord userFppsRecord = map.get(puid);
			if (userFppsRecord == null) {
				// 被推荐的人没有收益
				continue;
			}
			UserFeeRecord userFeeRecord = new UserFeeRecord();
			userFeeRecord.setUserId(userId);
			userFeeRecord.setDay(userFppsRecord.getDay());
			Float s = userFppsRecord.getFppsAfterFee() * recommendUser.getFeeRate();
			Long lo = BigDecimal.valueOf(s).longValue();
			userFeeRecord.setFee(lo);
			userFeeRecord.setCreateTime(date);
			userFeeRecord.setFeeType(2);
			userFeeRecord.setFeeDesc("推荐用户的收益");
			lis.add(userFeeRecord);
		}
		return lis;
	}
}
