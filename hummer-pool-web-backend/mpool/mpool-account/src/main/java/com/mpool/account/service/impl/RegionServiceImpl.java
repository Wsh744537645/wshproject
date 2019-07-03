package com.mpool.account.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mpool.account.mapper.RegionMapper;
import com.mpool.account.service.RegionService;
import com.mpool.common.model.account.Region;

@Service
public class RegionServiceImpl implements RegionService {
	@Autowired
	private RegionMapper regionMapper;

	@Override
	public List<Region> selectRegion() {
		return regionMapper.selectList(null);
	}

}
