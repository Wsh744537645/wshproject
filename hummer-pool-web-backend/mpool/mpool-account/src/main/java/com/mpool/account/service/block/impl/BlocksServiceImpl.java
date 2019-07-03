package com.mpool.account.service.block.impl;

import com.mpool.account.mapper.block.BlocksMapper;
import com.mpool.account.service.block.BlocksService;
import com.mpool.common.model.block.Blocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BlocksServiceImpl implements BlocksService {
    @Autowired
    private BlocksMapper blocksMapper;

    @Override
    @Transactional
    public void insertOne(Blocks blocks) {
        blocksMapper.insert(blocks);
    }

    @Override
    @Transactional
    public void insertBlocks(List<Blocks> list) {
        blocksMapper.insertBlocks(list);
    }

    @Override
    public int getCount() {
        return blocksMapper.getCount();
    }
}
