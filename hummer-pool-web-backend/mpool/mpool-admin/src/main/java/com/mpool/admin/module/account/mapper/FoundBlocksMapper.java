package com.mpool.admin.module.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.admin.mapperUtils.account.mapperBase.FoundBlocksBaseMapper;
import com.mpool.common.model.account.FoundBlocks;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FoundBlocksMapper extends BaseMapper<FoundBlocks>, FoundBlocksBaseMapper {

}
