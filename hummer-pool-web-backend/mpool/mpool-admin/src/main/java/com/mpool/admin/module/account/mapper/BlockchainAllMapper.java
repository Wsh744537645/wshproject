package com.mpool.admin.module.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.admin.mapperUtils.account.mapperBase.BlockchainAllBaseMapper;
import com.mpool.common.model.account.BlockchainAllModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlockchainAllMapper extends BaseMapper<BlockchainAllModel>, BlockchainAllBaseMapper {

}
