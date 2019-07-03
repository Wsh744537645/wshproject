package com.mpool.account.mapper.pool;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mpool.common.model.pool.MiningWorkers;

@Mapper
public interface MiningWorkersMapper {
	List<MiningWorkers> findByUserIdAndWorkerIds(@Param("userId") Long userId,
			@Param("workerIds") List<String> workerIds);

	MiningWorkers getMiningWorkers(@Param("workerId") Long workerId, @Param("userId") Long userId);

	List<Map<String, Object>> getUserWorkerActive();

	List<MiningWorkers> getUserShare();
}
