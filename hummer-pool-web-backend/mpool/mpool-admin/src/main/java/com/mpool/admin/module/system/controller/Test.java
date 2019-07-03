package com.mpool.admin.module.system.controller;

import com.mpool.admin.mapperUtils.pool.MiningWorkersMapperUtils;
import com.mpool.admin.module.account.service.UserService;
import com.mpool.admin.module.dashboard.service.DashboardService;
import com.mpool.admin.module.dashboard.service.PoolApiService;
import com.mpool.common.Result;
import com.mpool.common.exception.MpoolException;
import com.mpool.common.model.pool.MiningWorkers;
import com.mpool.common.model.pool.StatsPoolDay;
import com.mpool.common.model.pool.StatsUsersDay;
import com.mpool.common.model.pool.StatsWorkersDay;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RequestMapping("/wsh")
@Controller
public class Test {

    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private UserService userService;
    @Autowired
    private PoolApiService poolApiService;
    @Autowired
    private MiningWorkersMapperUtils miningWorkersMapperUtils;

    @GetMapping("get")
    @ResponseBody
    public Result getPoolPast30DayShare(Long userId) {
        MiningWorkers workers = miningWorkersMapperUtils.getUserMiningWorkers(userId);
        return Result.ok(workers);
    }

    @GetMapping("/getUser30Share")
    @ApiOperation("获取用户30天算力")
    @ResponseBody
    public Result getUser30ShareInfo(Long userId){
        List<StatsUsersDay> usersDayList = userService.getUser30DayShare(userId);
        return Result.ok(usersDayList);
    }

    @GetMapping("/getUser30Workers")
    @ApiOperation("获取用户30天矿机活跃数")
    @ResponseBody
    public Result getUser30WorkersInfo(Long userId){
        List<Map<String, Object>> usersDayList = userService.getUser30DayWorker(userId);
        return Result.ok(usersDayList);
    }

    @GetMapping("/getUser24HWorkers")
    @ApiOperation("获取用户30天矿机活跃数")
    @ResponseBody
    public Result getUser24HWorkersInfo(Long userId){
        List<Map<String, Object>> usersDayList = userService.getUser24HWorker(userId);
        return Result.ok(usersDayList);
    }

    @GetMapping("/getWorker24H")
    @ApiOperation("获取矿机24小时的算力")
    public Result getWorker24HInfo(@RequestParam("workerId") String workerId){
        Long id = Long.parseLong(workerId);
        List<Map<String, Object>> workerHourList = poolApiService.getWorker24HByWorkerId(id);
        return Result.ok(workerHourList);
    }

    @GetMapping("getMu")
    @ResponseBody
    public String getMu(){
        String url = "http://127.0.0.1:9000/multiple/get";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);

        return  result;
    }
}
