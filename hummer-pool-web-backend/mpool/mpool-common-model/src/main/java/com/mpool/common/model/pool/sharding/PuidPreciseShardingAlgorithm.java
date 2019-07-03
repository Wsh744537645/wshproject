package com.mpool.common.model.pool.sharding;

import java.io.Serializable;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

public class PuidPreciseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {
	private static final Logger log = LoggerFactory.getLogger(PuidPreciseShardingAlgorithm.class);

	public PuidPreciseShardingAlgorithm() {
		super();
		log.debug("PuidPreciseShardingAlgorithm init --------------------------->>>>>>>>>>>>>>");
	}

	@Override
	public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
		log.debug("shardingValue -> {} ,availableTargetNames-> {}", shardingValue, availableTargetNames);
		Object userId = shardingValue.getValue();
		//Serializable id = SecurityUtils.getSubject().getSession().getId();
		//log.debug("session id -> {}", id);
		for (String each : availableTargetNames) {
			log.debug("each -> {} ", each);
			return "ds0";
		}
		throw new UnsupportedOperationException();
	}

}