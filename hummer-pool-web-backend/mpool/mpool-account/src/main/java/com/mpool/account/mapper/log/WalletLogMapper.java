package com.mpool.account.mapper.log;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.log.WalletLog;

@Mapper
public interface WalletLogMapper extends BaseMapper<WalletLog> {

}
