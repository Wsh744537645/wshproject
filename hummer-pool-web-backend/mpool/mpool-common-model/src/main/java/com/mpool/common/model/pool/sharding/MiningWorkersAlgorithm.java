package com.mpool.common.model.pool.sharding;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

public class MiningWorkersAlgorithm implements PreciseShardingAlgorithm<Long> {

	private static final Logger log = LoggerFactory.getLogger(MiningWorkersAlgorithm.class);

	public MiningWorkersAlgorithm() {
		super();
		log.debug("MiningWorkersAlgorithm init --------------------------->>>>>>>>>>>>>>");
	}

	@Override
	public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
		log.debug("shardingValue -> {} ,availableTargetNames-> {}", shardingValue, availableTargetNames);
		Object userId = shardingValue.getValue();
		for (String each : availableTargetNames) {
			log.debug("each -> {} ", each);
			return "ds2";
		}
		throw new UnsupportedOperationException();
	}

}
