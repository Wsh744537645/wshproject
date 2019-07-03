package com.mpool.accountmultiple.mapper.fpps;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.fpps.FppsRatioDay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FppsRatioDayMapper extends BaseMapper<FppsRatioDay> {

	FppsRatioDay getRatio(@Param("day") Integer day);
}
