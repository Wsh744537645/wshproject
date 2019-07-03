package com.mpool.share.admin.module.bill.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.Result;
import com.mpool.common.model.share.UserBillToBePay;
import com.mpool.common.util.DateUtil;
import com.mpool.common.util.page.Page;
import com.mpool.common.util.page.PageModel;
import com.mpool.share.admin.model.User;
import com.mpool.share.admin.model.UserBillToBePayVO;
import com.mpool.share.admin.module.bill.service.UserBillService;
import com.mpool.share.admin.module.bill.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/6/18 17:47
 */

@RestController
@RequestMapping({"/bill", "/apis/bill"})
public class UserBillController {
    @Autowired
    private UserBillService userBillService;
    @Autowired
    private UserService userService;

    @GetMapping("/get/settlement")
    @ApiOperation("获得已结算账单列表")
    public Result getSettlementBillList(PageModel pageModel, String username){
        Long userId = null;
        if(username != null) {
            User user = userService.findByUsername(username);
            if(user != null){
                userId = user.getUserId();
            }
        }

        IPage<Map<String, Object>> page = new Page<>(pageModel);
        page = userBillService.getSettlementBillList(page, userId);
        return Result.ok(page);
    }

    @GetMapping("/get/tobepaid")
    @ApiOperation("获得已结算账单列表")
    public Result getTobePaidBillList(PageModel pageModel, Long begTime, Long endTime, Integer state){
        IPage<UserBillToBePayVO> page = new Page<>(pageModel);
        Date begt = null;
        Date endt = null;
        if(begTime != null && endTime != null) {
            begt = new Date(begTime * 1000);
            endt = new Date(endTime * 1000);
        }
        if(state == null){
            state = 100;
        }
        page = userBillService.getToBePaidBillList(page, begt, endt, state);
        return Result.ok(page);
    }

    @GetMapping("get/tobepaid/single")
    @ApiOperation("通过单号或者txid获得账单")
    public Result findToBePaidBill(String billId, String txid){
        UserBillToBePayVO vo = userBillService.findToBePaidBill(billId, txid);
        return Result.ok(vo);
    }

    @GetMapping("/get/hadpaid")
    @ApiOperation("获得已结算账单列表")
    public Result getHadPaidBillList(PageModel pageModel){
        IPage<Map<String, Object>> page = new Page<>(pageModel);
        page = userBillService.getHadPaidBillList(page);
        return Result.ok(page);
    }

    @PostMapping("/create/tobepaid")
    @ApiOperation("生成待支付账单")
    public Result createTobePaidBill(String[] billIds){
        List<String> list = new ArrayList<>();
        for(String billid : billIds){
            list.add(billid);
        }
        userBillService.createTobePaidBill(list);
        return Result.ok();
    }

    @PostMapping("/new/bill")
    @ApiOperation("新增账单")
    public Result newBill(String username, Double payNum, String discrible){
        userBillService.createExtraBill(username, payNum, discrible);
        return Result.ok();
    }

    @PostMapping("/set/txid")
    @ApiOperation("完成打款，设置账单txid")
    public Result setBillTxid(String paidId, String txid){
        userBillService.updateBillStateByTxid(paidId, txid);
        return Result.ok();
    }

    @GetMapping("get/bill/item/{paidId}")
    @ApiOperation("获得待支付账单下的所有账单列表")
    public Result getBillItems(@PathVariable("paidId") String paidId){
        return Result.ok(userBillService.getBillItems(paidId));
    }

    @PostMapping("/canal/topay/bill")
    @ApiOperation("撤销待支付账单")
    public Result canalToPayBill(String billId){
        userBillService.canalToPayBill(billId);
        return Result.ok();
    }
}
