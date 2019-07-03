package com.mpool.account.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.WorkerGroup;

@Mapper
public interface WorkerGroupMapper extends BaseMapper<WorkerGroup> {
	WorkerGroup findByWorkerGroupName(@Param("userId") Long userId, @Param("groupName") String groupName);

	List<WorkerGroup> selectListOrderByGroupSeq(@Param("workerGroup") WorkerGroup workerGroup);
}
