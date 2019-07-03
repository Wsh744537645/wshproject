package com.mpool.lucky.controller;

import com.mpool.common.Result;
import com.mpool.lucky.model.BlockLuckyModel;
import com.mpool.lucky.service.BlockLuckyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/8 11:50
 */

@RestController
@RequestMapping({ "/block", "/apis/block" })
public class BlockLuckyController {
    @Autowired
    private BlockLuckyService blockLuckyService;

    @GetMapping("/all")
    public Result getAll(){
        return Result.ok(blockLuckyService.getPoolLuckyAll());
    }

    @GetMapping("/month")
    public Result getMonth(){
        return Result.ok(blockLuckyService.getPoolLuckyMonth());
    }

    @GetMapping("/week")
    public Result getWeek(){
        return Result.ok(blockLuckyService.getPoolLuckyWeek());
    }

    @GetMapping("/monday/day180")
    public Result getMonday180(){
        return Result.ok(blockLuckyService.getHalfYearMondayPreWeek());
    }

    @GetMapping("/allA")
    public List<BlockLuckyModel> getAllA(){
        return blockLuckyService.getPoolLuckyAll();
    }

    @GetMapping("/monthA")
    public List<BlockLuckyModel> getMonthA(){
        return blockLuckyService.getPoolLuckyMonth();
    }

    @GetMapping("/weekA")
    public List<BlockLuckyModel> getWeekA(){
        return blockLuckyService.getPoolLuckyWeek();
    }

    @GetMapping("/monday/day180A")
    public List<BlockLuckyModel> getMonday180A(){
        return blockLuckyService.getHalfYearMondayPreWeek();
    }
}
