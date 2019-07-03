package com.mpool.account.service.block;

import com.mpool.common.model.block.BlockDifficulty;

import java.util.List;
import java.util.Map;

public interface BlockDifficultyService {
    void insertOne(BlockDifficulty blocks);
    void updateOne(BlockDifficulty blocks);
    /**
     * 批量处理数据
     * @param list
     */
    void insertList(List<BlockDifficulty> list);

    /**
     * 获得最新的难度数据
     * @return
     */
    Map<String, Object> getNewBlock();

    /**
     * 获取所有的难度数据
     * @return
     */
    List<BlockDifficulty> getAll();
}
