package com.mpool.share.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.log.UserLoginLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserLoginLogMapper extends BaseMapper<UserLoginLog>{

}
