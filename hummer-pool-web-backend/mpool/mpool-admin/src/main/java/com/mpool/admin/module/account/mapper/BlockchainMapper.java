package com.mpool.admin.module.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.admin.mapperUtils.account.mapperBase.BlockchainBaseMapper;
import com.mpool.common.model.account.Blockchain;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlockchainMapper extends BaseMapper<Blockchain>, BlockchainBaseMapper {



}
