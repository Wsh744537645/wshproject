package com.mpool.admin.module.account.mapper;

import com.mpool.admin.mapperUtils.account.mapperBase.FppsRatioDayBaseMapper;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.fpps.FppsRatioDay;

@Mapper
public interface FppsRatioDayMapper extends BaseMapper<FppsRatioDay>, FppsRatioDayBaseMapper {

}
