package com.mpool.account.service.impl;

import com.mpool.account.mapper.fpps.FppsRatioDayMapper;
import com.mpool.account.service.FppsRatioService;
import com.mpool.common.model.account.fpps.FppsRatioDay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FppsRatioServiceImpl implements FppsRatioService {
	private final static Float fZero = Float.valueOf(0.0f);
	private static final Logger log = LoggerFactory.getLogger(FppsRatioServiceImpl.class);

	@Autowired
	FppsRatioDayMapper fppsRatioDayMapper;

	@Override
	public Float getRatio(Integer day) {
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

}
