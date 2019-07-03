package com.mpool.account.mapper.fpps;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.fpps.FppsRatioDay;

@Mapper
public interface FppsRatioDayMapper extends BaseMapper<FppsRatioDay> {

	FppsRatioDay getRatio(@Param("day") Integer day);
}
