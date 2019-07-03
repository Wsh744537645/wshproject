package com.mpool.admin.mapperUtils.recommend.mapperBase;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.account.UserFeeRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/14 16:27
 */
public interface RecommendApartCoinBaseMapper {
    IPage<UserFeeRecord> getHistoryApartCoin(IPage<UserFeeRecord> page, @Param("str") Date str, @Param("end") Date end);

//	List<UserFeeRecord> getHistoryApartCoinType2Sum(@Param("str") Date str, @Param("end") Date end);

    List<UserFeeRecord> getHistoryRecommendApartCoin(@Param("days") List<Integer> days);

    Long sumApartCoin(@Param("feeType") Integer type,@Param("str") Date str, @Param("end") Date end);

    Long getCurrentCoin(@Param("day") Date day);
}
