package com.mpool.account.mapper.log;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.log.UserRegLog;

@Mapper
public interface UserRegLogMapper extends BaseMapper<UserRegLog> {

}
