package com.mpool.admin.module_zec.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.admin.mapperUtils.account.mapperBase.BlockchainBaseMapper;
import com.mpool.admin.module_zec.account.model.BlockchainZec;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlockchainZecMapper extends BaseMapper<BlockchainZec>, BlockchainBaseMapper {

}
