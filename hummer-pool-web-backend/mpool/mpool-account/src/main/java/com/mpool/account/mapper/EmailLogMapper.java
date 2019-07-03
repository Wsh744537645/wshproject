package com.mpool.account.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.log.EmailLog;

@Mapper
public interface EmailLogMapper extends BaseMapper<EmailLog> {
 
}
