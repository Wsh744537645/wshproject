package com.mpool.lucky.service.impl;

import com.mpool.lucky.mapper.BlockLuckyMapper;
import com.mpool.lucky.model.BlockLuckyModel;
import com.mpool.lucky.service.BlockLuckyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/8 11:42
 */

@Service
@Slf4j
public class BlockLuckyServiceImpl implements BlockLuckyService {
    @Autowired
    private BlockLuckyMapper blockLuckyMapper;

    @Override
    public List<BlockLuckyModel> getPoolLuckyAll() {
        return blockLuckyMapper.getPoolLuckyAll();
    }

    @Override
    public List<BlockLuckyModel> getPoolLuckyMonth() {
        return blockLuckyMapper.getPoolLuckyMonth();
    }

    @Override
    public List<BlockLuckyModel> getPoolLuckyWeek() {
        return blockLuckyMapper.getPoolLuckyWeek(100);
    }

    @Override
    public List<BlockLuckyModel> getHalfYearMondayPreWeek() {
        List<BlockLuckyModel> list = blockLuckyMapper.getPoolLuckyWeek(190);
        List<BlockLuckyModel> result = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
        for(BlockLuckyModel block : list){
            Date date = block.getCreatAt();
            String curSun = dateFormat.format(date);
            if(curSun.equals("Monday") || curSun.equals("星期一")){
                log.info("time={}, value={}", date, block);
                result.add(block);
            }
        }
        return result;
    }
}
