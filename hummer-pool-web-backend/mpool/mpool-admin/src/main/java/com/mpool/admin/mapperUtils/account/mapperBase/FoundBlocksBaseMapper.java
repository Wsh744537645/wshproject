package com.mpool.admin.mapperUtils.account.mapperBase;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.account.FoundBlocks;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/13 17:15
 */
public interface FoundBlocksBaseMapper {
    Integer getBlock();

    Long getRevenue();

    Integer getNowBlock(@Param("now") Date now);

    Long getNowRevenue(@Param("now")Date now);

    List<FoundBlocks> getHistoryBlock();

    //按时间段搜索爆块记录传参
    IPage<FoundBlocks> getHistoryBlockList(IPage<FoundBlocks> page, @Param("strTime")Date strTime, @Param("endTime")Date endTime);
    IPage<FoundBlocks> getHistoryBlocks(IPage<FoundBlocks> page);

    //导出按时间段搜索爆块记录
    List<FoundBlocks> exportHistoryBlockList(@Param("strTime")Date strTime, @Param("endTime")Date endTime);
}
