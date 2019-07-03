package com.mpool.account.controller;

import com.mpool.account.service.BlockchainService;
import com.mpool.account.service.MinerService;
import com.mpool.account.service.block.BlockDifficultyService;
import com.mpool.common.Result;
import com.mpool.common.model.block.BlockDifficulty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping({ "/miner", "/apis/miner" })
public class MinerController {
    @Autowired
    private MinerService minerService;
    @Autowired
    private BlockchainService blockchainService;
    @Autowired
    public BlockDifficultyService blockDifficultyService;

    @GetMapping("/btcMiners")
    @ApiOperation("获得btc矿机列表")
    public Result getBtcMinerList(){
        List<Map<String, Object>> list = minerService.getMinerListByCurrencyId(1);
        Map<String, Object> result = new HashMap<>();
        result.put("shareRate", 0.00003958);
        result.put("list", list);
        return Result.ok(result);
    }

    @GetMapping("/getListBycid")
    @ApiOperation("通过币种id获得矿机列表")
    public Result getMinerListByCurrencyId(@RequestParam(value = "cid") int cid){
        Map<String, Object> result = new HashMap<>();
        result.put("shareRate", 0.00003958);
        List<Map<String, Object>> list = minerService.getMinerListByCurrencyId(cid);
        result.put("list", list);
        return Result.ok(result);
    }

    @GetMapping("/getBlockChainInfo")
    @ApiOperation("获得全网算力和比特币价值")
    public Result getBlockChainInfo(){
        Map<String, Object> map = new HashMap<>();
        map.put("share", blockchainService.getBlockchainInfoSole().get("hash_rate"));
        map.put("price_usd", minerService.getCurBtcPriceToUsd());
        return Result.ok(map);
    }

    @GetMapping("/getBlockDiff")
    @ApiOperation("获得区块难度列表")
    public Result getBlockDiffList(){
        List<BlockDifficulty> list = blockDifficultyService.getAll();
        List<Map<String, Object>> mapList = new ArrayList<>();
        for(BlockDifficulty block : list){
            Map<String, Object> map = new HashMap<>();
            Long time = block.getTime().getTime() / 1000;
            map.put("hegiht", block.getHeight());
            map.put("difficulty", block.getDifficulty());
            map.put("time", time.longValue());

            mapList.add(map);
        }
        return Result.ok(mapList);
    }
}
