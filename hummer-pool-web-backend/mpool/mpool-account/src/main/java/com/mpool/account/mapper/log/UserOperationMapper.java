package com.mpool.account.mapper.log;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.log.LogUserOperation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserOperationMapper extends BaseMapper<LogUserOperation> {

}
