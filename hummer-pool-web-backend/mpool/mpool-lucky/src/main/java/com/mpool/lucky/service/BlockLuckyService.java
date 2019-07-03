package com.mpool.lucky.service;

import com.mpool.lucky.model.BlockLuckyModel;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/8 11:42
 */
public interface BlockLuckyService {
    List<BlockLuckyModel> getPoolLuckyAll();

    List<BlockLuckyModel> getPoolLuckyMonth();

    List<BlockLuckyModel> getPoolLuckyWeek();

    /**
     * 获取过去半年每周一的数据列表
     * @return
     */
    List<BlockLuckyModel> getHalfYearMondayPreWeek();
}
