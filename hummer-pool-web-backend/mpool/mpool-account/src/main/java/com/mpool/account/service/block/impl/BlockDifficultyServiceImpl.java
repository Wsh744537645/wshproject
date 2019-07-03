package com.mpool.account.service.block.impl;

import com.mpool.account.mapper.block.BlockDifficultyMapper;
import com.mpool.account.service.block.BlockDifficultyService;
import com.mpool.common.model.block.BlockDifficulty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class BlockDifficultyServiceImpl implements BlockDifficultyService {
    @Autowired
    private BlockDifficultyMapper blockDifficultyMapper;

    @Override
    @Transactional
    public void insertOne(BlockDifficulty blocks) {
        blockDifficultyMapper.insert(blocks);
    }

    @Override
    @Transactional
    public void updateOne(BlockDifficulty blocks) {
        blockDifficultyMapper.updateOneByDiff(blocks);
    }

    @Override
    @Transactional
    public void insertList(List<BlockDifficulty> list) {
//        if(list.isEmpty()){
//            return;
//        }
//        if(list.size() < 2){
//            blockDifficultyMapper.insert(list.get(0));
//        }else {
            blockDifficultyMapper.insertList(list);
//        }
    }

    @Override
    public Map<String, Object> getNewBlock() {
        return blockDifficultyMapper.getNewBlock();
    }

    @Override
    public List<BlockDifficulty> getAll() {
        return blockDifficultyMapper.getAll();
    }
}
