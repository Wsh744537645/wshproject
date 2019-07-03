package com.mpool.admin.module_zec.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.admin.mapperUtils.account.mapperBase.FppsRatioDayBaseMapper;
import com.mpool.admin.module_zec.account.model.FppsRatioDayZec;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FppsRatioDayZecMapper extends BaseMapper<FppsRatioDayZec>, FppsRatioDayBaseMapper {

}
