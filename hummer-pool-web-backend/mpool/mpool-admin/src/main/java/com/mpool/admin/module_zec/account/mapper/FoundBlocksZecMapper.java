package com.mpool.admin.module_zec.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.admin.mapperUtils.account.mapperBase.FoundBlocksBaseMapper;
import com.mpool.admin.module_zec.account.model.FoundBlocksZec;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FoundBlocksZecMapper extends BaseMapper<FoundBlocksZec>, FoundBlocksBaseMapper {

}
