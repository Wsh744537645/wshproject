package com.mpool.share.admin.module.dashboard.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.Result;
import com.mpool.common.util.page.Page;
import com.mpool.common.util.page.PageModel;
import com.mpool.share.admin.module.bill.service.UserBillService;
import com.mpool.share.admin.module.bill.service.UserService;
import com.mpool.share.admin.module.dashboard.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/6/25 16:44
 */

@RestController
@RequestMapping({"/dashboard", "/apis/dashboard"})
public class DashboardController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserBillService userBillService;
    @Autowired
    private UserService userService;

    @GetMapping("/getall")
    @ApiOperation("获得所有数据统计汇总")
    public Result getTotalData(){
        Double totlePay = orderService.getTotalPay();
        Double powerpay = orderService.getTotalPowerPay();
        Double recommendShare = userBillService.getRecommendShareTotal();
        Map<String, Object> map = userBillService.getAdminPay();
        map.put("total_ revenue", totlePay);
        map.put("total_power_revenue", powerpay);
        map.put("total_recommend_revenue", recommendShare);
        return Result.ok(map);
    }

    @GetMapping("/get/orderlist")
    @ApiOperation("获得所有订单详情")
    public Result getOrderList(PageModel pageModel, String productName, String orderId, String userName){
        IPage<Map<String, Object>> page = new Page<>(pageModel);
        page = orderService.getHadPayOrderList(page, productName, orderId, userName);
        return Result.ok(page);
    }

    @GetMapping("/get/powerorderlist")
    @ApiOperation("获得所有电费订单详情")
    public Result getPowerOrderList(PageModel pageModel, String productName, String orderId, String userName){
        IPage<Map<String, Object>> page = new Page<>(pageModel);
        page = orderService.getHadPayPowerOrderList(page, productName, orderId, userName);
        return Result.ok(page);
    }

    @GetMapping("/get/recommendlist")
    @ApiOperation("获得返佣详情列表")
    public Result getRecommendList(PageModel pageModel, String username, Long begTime, Long endTime){
        IPage<Map<String, Object>> page = new Page<>(pageModel);
        Date begt = null;
        Date endt = null;
        if(begTime != null && endTime != null){
            begt = new Date(begTime * 1000);
            endt = new Date(endTime * 1000);
        }
        if(username != null && username.isEmpty()){
            username = null;
        }
        page = userBillService.getRecommendList(page, begt, endt, username);
        return Result.ok(page);
    }

    @GetMapping("/get/accept/adminpay/list")
    @ApiOperation("获得算力订单及总分币数详情列表")
    public Result getAcceptOrderAdminPayList(PageModel pageModel, String productName, Long begTime, Long endTime){
        IPage<Map<String, Object>> page = new Page<>(pageModel);
        Date begt = null;
        Date endt = null;
        if(begTime != null && endTime != null){
            begt = new Date(begTime * 1000);
            endt = new Date(endTime * 1000);
        }
        if(productName != null && productName.isEmpty()){
            productName = null;
        }
        page = userBillService.getAcceptOrderAdminPayList(page, begt, endt, productName);
        return Result.ok(page);
    }

    @GetMapping("/get/users")
    @ApiOperation("获得用户信息")
    public Result getUsersInfo(PageModel pageModel, String username){
        IPage<Map<String, Object>> page = new Page<>(pageModel);
        if(username != null && username.isEmpty()){
            username = null;
        }
        page = userService.getUsersInfoList(page, username);

        return Result.ok(page);
    }

    @PostMapping("/reset/passowrd")
    @ApiOperation("重置用户密码")
    public Result resetUserPassword(Long userId, String password){
        userService.resetPassword(userId, password);
        return Result.ok();
    }
}
