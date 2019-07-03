package com.mpool.accountmultiple.service.fpps.impl;

import com.mpool.accountmultiple.mapper.fpps.BlockRewardMapper;
import com.mpool.accountmultiple.mapper.fpps.FppsRatioDayMapper;
import com.mpool.accountmultiple.service.fpps.FppsRatioDayService;
import com.mpool.common.model.account.fpps.BlockReward;
import com.mpool.common.model.account.fpps.FppsRatioDay;
import com.mpool.common.util.DateUtil;
import com.mpool.common.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class FppsRatioDayServiceImpl implements FppsRatioDayService {

	private static final Logger log = LoggerFactory.getLogger(FppsRatioDayServiceImpl.class);

	@Autowired
	private FppsRatioDayMapper fppsRatioDayMapper;
	@Autowired
	private BlockRewardMapper blockRewardMapper;

	@Override
	public void insert(FppsRatioDay fppsRatioDay) {
		Date date = DateUtil.addDay(new Date(), 1);
		String yyyymMdd = DateUtil.getYYYYMMdd(date);
		Integer day = Integer.parseInt(yyyymMdd);
		List<BlockReward> blockRewards = blockRewardMapper.getBlockReward();
		float ratioCount = 0f;
		Integer heightNum = blockRewards.size();
		Integer maxHeight = Integer.MIN_VALUE;
		Integer minHeight = Integer.MAX_VALUE;
		for (BlockReward blockReward : blockRewards) {
			Integer height = blockReward.getHeight();
			Long rewardBlock = blockReward.getRewardBlock();
			Long rewardFees = blockReward.getRewardFees();
			float f = BigDecimal.valueOf(rewardBlock).add(BigDecimal.valueOf(rewardFees))
					.divide(BigDecimal.valueOf(rewardBlock)).floatValue();
			ratioCount += f;
			if (height < minHeight) {
				minHeight = height;
			}
			if (height > maxHeight) {
				maxHeight = height;
			}
		}

		//float ratio = ratioCount / heightNum;
		// 应该是1.15以内的数字，偏差太大看是不是算错了
//		if (ratio > 1.15f) {
//			log.warn("ratio > 1.15");
//			ratio = 1.15f;
//		}
		float ratio = 1.0f;

		fppsRatioDay.setDay(day);
		fppsRatioDay.setHeightBegin(minHeight);
		fppsRatioDay.setHeightEnd(maxHeight);
		fppsRatioDay.setHeightNum(heightNum);
		fppsRatioDay.setRatio(ratio);
		log.info("blockRewards => {} ", JSONUtil.toJson(blockRewards));
		log.info("heightNum={},maxHeight={},minHeight={},ratioCount={},ratio={}", heightNum, maxHeight, minHeight,
				ratioCount, ratio);
		log.info("fppsRatioDay={}", JSONUtil.toJson(fppsRatioDay));
		fppsRatioDayMapper.insert(fppsRatioDay);
	}

	@Override
	public FppsRatioDay findByDay(Integer day) {
		// TODO Auto-generated method stub
		return null;
	}

}
