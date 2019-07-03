package com.mpool.admin.module.pool.service.impl;

import java.util.List;

import com.mpool.admin.mapperUtils.pool.StatsUsersDayMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mpool.admin.module.pool.mapper.StatsUsersDayMapper;
import com.mpool.admin.module.pool.service.StatusUsersDayService;

@Service
public class StatsUsersDayServiceImpl implements StatusUsersDayService {
	@Autowired
	private StatsUsersDayMapperUtils statsUsersDayMapper;

	@Override
	public List<Long> getShareByPuidAndDay(List<Long> puid, Long day) {
		return statsUsersDayMapper.getShareByPuidAndDay(puid, day);
	}

}
