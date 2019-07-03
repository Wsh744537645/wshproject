package com.mpool.admin.module.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.log.LogUserOperation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogAdminOperationMpper extends BaseMapper<LogUserOperation> {
}
