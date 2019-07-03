package com.mpool.admin.module.account.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mpool.admin.mapperUtils.account.FppsRatioDayMapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.InModel.InBlock;
import com.mpool.admin.module.account.service.FppsRatioDayService;
import com.mpool.common.model.account.fpps.FppsRatioDay;
import com.mpool.common.util.DateUtil;

@Service
public class FppsRatioDayServiceImpl implements FppsRatioDayService {

	private static final Logger log = LoggerFactory.getLogger(FppsRatioDayServiceImpl.class);

	@Autowired
	private FppsRatioDayMapperUtils fppsRatioDayMapperUtils;

	@Override
	public void updateFppsRateDay(Float fppsRate) {

		String da = DateUtil.getYYYYMMdd(new Date());
		Integer day = Integer.parseInt(da);
		log.debug("feeRate => {},day=>{}", fppsRate, day);
		fppsRatioDayMapperUtils.updateFppsRateDay(day, fppsRate);
	}


	@Override
	public List<List<Object>> exportHistoryFppsRateDays(InBlock date, IPage<FppsRatioDay> page) {

		List<List<Object>> list = new ArrayList<>();
		IPage<FppsRatioDay> pageList = getMinerFeeRateList(page, date);
		List<FppsRatioDay> lis = pageList.getRecords();
		for (FppsRatioDay li : lis) {
			List<Object> objects = new ArrayList<>();
			objects.add(li.getDay());
			// 旧费率
			objects.add(li.getRatio());
			// 新费率
			objects.add(li.getNewRatio());
			list.add(objects);
		}
		return list;
	}

	@Override
	public IPage<FppsRatioDay> getMinerFeeRateList(IPage<FppsRatioDay> iPage, InBlock inblock) {
		Date str = inblock.getStrTime();
		Date end = inblock.getEndTime();
		if (str!=null && end!=null){
			Long strT =	str.getTime() + 8 * 60 * 60 * 1000;
			Long endT = end.getTime()+ 8 * 60 * 60 * 1000;
			Date strTime = new Date(strT);//用Date构造方法，将long转Date
			Date endTime = new Date(endT);//用Date构造方法，将long转Date
			return fppsRatioDayMapperUtils.getFppsRatioDay(iPage, strTime,endTime);
		}

		return fppsRatioDayMapperUtils.getFppsRatioDay(iPage, str,end);
	}
}
