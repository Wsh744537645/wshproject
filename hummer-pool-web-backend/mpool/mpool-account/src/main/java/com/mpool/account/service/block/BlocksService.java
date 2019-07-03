package com.mpool.account.service.block;

import com.mpool.common.model.block.Blocks;

import java.util.List;

public interface BlocksService {

    void insertOne(Blocks blocks);

    /**
     * 批量处理数据
     * @param list
     */
    void insertBlocks(List<Blocks> list);

    /**
     * 获取表的数量
     * @return
     */
    int getCount();
}
