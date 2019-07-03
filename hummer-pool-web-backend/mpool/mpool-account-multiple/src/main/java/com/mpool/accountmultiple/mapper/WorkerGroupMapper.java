package com.mpool.accountmultiple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.WorkerGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WorkerGroupMapper extends BaseMapper<WorkerGroup> {
	WorkerGroup findByWorkerGroupName(@Param("userId") Long userId, @Param("groupName") String groupName);

	List<WorkerGroup> selectListOrderByGroupSeq(@Param("workerGroup") WorkerGroup workerGroup);
}
