package com.mpool.admin.module_zec.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.admin.mapperUtils.account.mapperBase.BlockchainAllBaseMapper;
import com.mpool.admin.module_zec.account.model.BlockchainAllZecModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlockchainAllZecMapper extends BaseMapper<BlockchainAllZecModel>, BlockchainAllBaseMapper {

}
