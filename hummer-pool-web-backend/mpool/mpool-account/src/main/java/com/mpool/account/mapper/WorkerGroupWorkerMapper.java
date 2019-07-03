package com.mpool.account.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.WorkerGroupWorker;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface WorkerGroupWorkerMapper extends BaseMapper<WorkerGroupWorker> {

	void deleteWorkerGroup(@Param("workerIds") List<String> workerIds, @Param("userId") Long userId);

	@Select("select count(*) from account_worker__worker_group where worker_id=#{workerId} and user_id=#{userId} and group_id=#{groupId}")
	int getWorkerCount(@Param("workerId") Long workerId, @Param("userId") Long userId, @Param("groupId") Integer groupId);
}
