package com.mpool.share.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.Result;
import com.mpool.common.util.DateUtil;
import com.mpool.common.util.page.Page;
import com.mpool.common.util.page.PageModel;
import com.mpool.share.service.RealTimeDataService;
import com.mpool.share.service.UserBillService;
import com.mpool.share.service.UserPayService;
import com.mpool.share.service.order.OrderAcceptService;
import com.mpool.share.utils.AccountSecurityUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/5/30 14:21
 */

@RestController
@RequestMapping({"/bill", "apis/bill"})
public class BillController {
    @Autowired
    private UserBillService userBillService;
    @Autowired
    private UserPayService userPayService;
    @Autowired
    private OrderAcceptService orderAcceptService;
    @Autowired
    private RealTimeDataService realTimeDataService;

    @GetMapping("/daily/time")
    @ApiOperation("获得时间范围内的产出")
    public Result getBillByDailyTime(Long begTime, Long endTime, PageModel pageModel){
        IPage<Map<String, Object>> page = new Page<>(pageModel);
        Long userId = AccountSecurityUtils.getUser().getUserId();
        Long timeB = Long.parseLong(DateUtil.getYYYYMMdd(new Date(begTime * 1000)));
        Long timeE = Long.parseLong(DateUtil.getYYYYMMdd(new Date(endTime * 1000)));
        page = userBillService.getDailyAcceptOrderList(page, userId, timeB, timeE);
        return Result.ok(page);
    }

    @GetMapping("/daily/day")
    @ApiOperation("获得时间范围内的产出,近day天,不传显示全部")
    public Result getBillByDailyDay(Integer day, PageModel pageModel){
        IPage<Map<String, Object>> page = new Page<>(pageModel);
        Long userId = AccountSecurityUtils.getUser().getUserId();
        if(day.equals(0)){
            day = 365 * 2;
        }
        Date date = new Date();
        Long timeB = Long.parseLong(DateUtil.getYYYYMMdd(DateUtil.addDay(date, 0 - day)));
        Long timeE = Long.parseLong(DateUtil.getYYYYMMdd(date));
        page = userBillService.getDailyAcceptOrderList(page, userId, timeB, timeE);
        return Result.ok(page);
    }

    @GetMapping("/get/order")
    @ApiOperation("通过订单号获得账单列表，orderId:'all'获得全部")
    public Result getBillByOrderId(String orderId, Integer state, PageModel pageModel){
        IPage<Map<String, Object>> page = new Page<>(pageModel);
        Long userId = AccountSecurityUtils.getUser().getUserId();
        if(orderId.equals("all")){
            orderId = null;
        }
        if(!state.equals(0) && !state.equals(3)){
            state = null;
        }
        page = userBillService.getBillListByOrderid(page, state, userId, orderId);
        return Result.ok(page);
    }

    @GetMapping("/total")
    @ApiOperation("获得总算力和当前余额")
    public Result getRealTimeInfo(){
        Long userId = AccountSecurityUtils.getUser().getUserId();
        Double balance = userPayService.getUserBalance(userId);
        Map<String, Object> map = orderAcceptService.getCurAcceptTotal(userId);
        if(map == null){
            map = new HashMap<>();
            map.put("size", 0);
            map.put("accept", 0);
        }
        Double usd = realTimeDataService.getCurCurrencyPriceToUsd(1) * balance;
        map.put("balance", balance);
        map.put("balance_usd", usd);
        return Result.ok(map);
    }
}
