package com.mpool.admin.module.log.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.log.SmsLog;

@Mapper
public interface SmsLogMapper extends BaseMapper<SmsLog> {

}
