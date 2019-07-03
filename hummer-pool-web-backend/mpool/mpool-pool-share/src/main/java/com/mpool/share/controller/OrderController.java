package com.mpool.share.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.Result;
import com.mpool.common.util.page.Page;
import com.mpool.common.util.page.PageModel;
import com.mpool.share.model.OrderCreateModel;
import com.mpool.share.resultVo.OrderListVO;
import com.mpool.share.resultVo.OrderCreateVO;
import com.mpool.share.service.order.OrderAcceptService;
import com.mpool.share.service.order.OrderPowerService;
import com.mpool.share.service.order.OrderService;
import com.mpool.share.utils.AccountSecurityUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/5/23 11:26
 */

@RestController
@RequestMapping({"/order", "apis/order"})
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderAcceptService orderAcceptService;
    @Autowired
    private OrderPowerService orderPowerService;

    /**
     * 创建订单
     * @param model
     * @return
     */
    @PostMapping("/create/order")
    @ApiOperation("创建订单")
    public Result createOrder(@Valid @RequestBody OrderCreateModel model){
        OrderCreateVO orderVO = orderService.createOrder(model);
        return Result.ok(orderVO);
    }

    /**
     * 获得订单列表
     * @param type
     * @param state
     * @param pageModel
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("获得订单列表")
    public Result getOrderList(Integer type, Integer state, PageModel pageModel){
        IPage<OrderListVO> page = new Page<>(pageModel);
        Long userId = AccountSecurityUtils.getUser().getUserId();
        if(state.equals(100)){
            state = null;
        }
        if(type == null || type.equals(1)){
            orderAcceptService.getAcceptOrderList(userId, page, state);
        }else{
            orderPowerService.getPowerOrderList(userId,page, state);
        }
        return Result.ok(page);
    }

    @GetMapping("/accept/normal")
    @ApiOperation("算力订单基本信息")
    public Result getAcceptNormalByOrderId(@RequestParam("orderId") String orderId){
        Map<String, Object> map = orderAcceptService.getAcceptNormalByOrderId(AccountSecurityUtils.getUser().getUserId(), orderId);
        return Result.ok(map);
    }

    @GetMapping("/accept/paystate")
    @ApiOperation("算力订单支付信息")
    public Result getAcceptPayInfo(@RequestParam("orderId") String orderId){
        Map<String, Object> map = orderAcceptService.getOrderPayInfo(AccountSecurityUtils.getUser().getUserId(), orderId);
        return Result.ok(map);
    }

    @GetMapping("/accept/share")
    @ApiOperation("算力订单产出信息")
    public Result getAcceptShareInfo(@RequestParam("orderId") String orderId){
        Map<String, Object> map = orderAcceptService.getOrderShareInfo(AccountSecurityUtils.getUser().getUserId(), orderId);
        return Result.ok(map);
    }
}
