package com.mpool.account.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.account.model.CurrentWorkerStatus;
import com.mpool.common.model.account.Worker;

@Mapper
public interface WorkerMapper extends BaseMapper<Worker> {
	IPage<Map<String, Object>> selectWorkerIdAndWorkerName(IPage<Map<String, Object>> p, @Param("worker") Worker worker,
														   @Param("groupId") Integer groupId, @Param("date")Date date);

	List<Worker> countUserWorkerStatusBatch(@Param("userIds") List<Long> userIds);

	List<Worker> exprotData(@Param("userId") Long userId);

	Worker getWorkerInfo(Long id);

	void deleteByWorkerId(@Param("userId") String userId, @Param("id") Long id);

	List<CurrentWorkerStatus> getAllUserWorkerActive(@Param("date")Date date);
}
