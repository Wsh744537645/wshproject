package com.mpool.admin.mapperUtils.account.mapperBase;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.account.fpps.FppsRatioDay;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @Author: stephen
 * @Date: 2019/5/13 17:30
 */
public interface FppsRatioDayBaseMapper {
    IPage<FppsRatioDay> getFppsRatioDay(IPage<FppsRatioDay> iPage, @Param("strTime")Date strTime, @Param("endTime")Date endTime);

    /**
     * wgg 修改昨天的费率
     *
     * @param day
     * @param fppsRate
     */
    void updateFppsRateDay(@Param("day") Integer day, @Param("fppsRate") Float fppsRate);

    IPage<FppsRatioDay> getFppsRateDays(IPage<FppsRatioDay> page, @Param("str") Integer str, @Param("end") Integer end);
}
