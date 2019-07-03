package com.mpool.common.model.pool.sharding;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.shardingsphere.api.algorithm.sharding.ShardingValue;
import io.shardingsphere.api.algorithm.sharding.hint.HintShardingAlgorithm;

public class RegionIdHintShardingStrategy implements HintShardingAlgorithm {

	private static final Logger log = LoggerFactory.getLogger(RegionIdHintShardingStrategy.class);

	public RegionIdHintShardingStrategy() {
		super();
		log.debug("----------------->init RegionIdHintShardingStrategy");
	}

	@Override
	public Collection<String> doSharding(Collection<String> availableTargetNames, ShardingValue shardingValue) {
		// HintShardingStrategy
		log.debug("availableTargetNames -> {}", availableTargetNames);

		ArrayList<String> arrayList = new ArrayList<>();
		arrayList.add("ds" + getRegionId());
		return arrayList;
	}

	public String getRegionId() {
		return "0";
	}

}
