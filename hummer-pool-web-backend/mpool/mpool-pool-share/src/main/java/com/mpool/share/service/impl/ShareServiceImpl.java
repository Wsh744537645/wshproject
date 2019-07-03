package com.mpool.share.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpool.common.model.billenem.BillEnableEnum;
import com.mpool.common.model.billenem.BillTypeEnum;
import com.mpool.common.model.pool.utils.MathShare;
import com.mpool.common.model.product.OrderPowerModel;
import com.mpool.common.model.product.WalletAddressModel;
import com.mpool.common.model.share.*;
import com.mpool.common.util.DateUtil;
import com.mpool.share.mapper.PowerSpendMapper;
import com.mpool.share.mapper.ShareRateMapper;
import com.mpool.share.mapper.UserPayMapper;
import com.mpool.share.mapper.order.OrderMapper;
import com.mpool.share.mapper.order.OrderPowerMapper;
import com.mpool.share.mapper.product.ProductStateMapper;
import com.mpool.share.mapper.recommend.RecommendBillItemMapper;
import com.mpool.share.mapper.recommend.UserInviteMapper;
import com.mpool.share.model.*;
import com.mpool.share.service.BtcRpcService;
import com.mpool.share.service.ShareService;
import com.mpool.share.service.UserBillService;
import com.mpool.share.service.WalletAddressService;
import com.mpool.share.service.product.ProductService;
import com.mpool.share.task.OrderNotifyTask;
import com.mpool.share.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * @Author: stephen
 * @Date: 2019/5/27 17:36
 */

@Service
@Slf4j
public class ShareServiceImpl implements ShareService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserBillService userBillService;
    @Autowired
    private OrderPowerMapper orderPowerMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserPayMapper userPayMapper;
    @Autowired
    private BtcRpcService btcRpcService;
    @Autowired
    private ShareRateMapper shareRateMapper;
    @Autowired
    private WalletAddressService walletAddressService;
    @Autowired
    private UserInviteMapper inviteMapper;
    @Autowired
    private RecommendBillItemMapper billItemMapper;
    @Autowired
    private PowerSpendMapper powerSpendMapper;
    @Autowired
    private ExecutorService executorService;
    @Autowired
    private OrderNotifyTask orderNotifyTask;
    @Autowired
    private ProductStateMapper productStateMapper;

    @Override
    public void taskShareRate() {
        Long diff = btcRpcService.getDifficult();
        Double rate = MathShare.pow10 * MathShare.DAY * 12.5D / (MathShare.pow2 * diff);
        Date date = DateUtil.addDay(new Date(), -1);
        String day = DateUtil.getYYYYMMdd(date);
        ShareRateModel model = new ShareRateModel();
        model.setDay(day);
        model.setRate(rate);
        model.setDiff(diff);
        model.setCreateTime(new Date());

        shareRateMapper.insert(model);
    }

    @Override
    @Transactional
    public void taskSettlement() {
        Date date = new Date();
        String ymd = DateUtil.getYYYYMMdd(date);
        String curDay = DateUtil.getYYYYMMdd(DateUtil.addDay(date, -1));
        List<String> orderIds = new ArrayList<>();

        //计算收益
        ShareRateModel shareRateModel = new ShareRateModel();
        shareRateModel.setDay(curDay);
        shareRateModel = shareRateMapper.selectOne(new QueryWrapper<>(shareRateModel));
        Double rate = shareRateModel.getRate();

        //记录用户的总收益
       // Map<Long, Double> shareMap = new HashMap<>();

        List<UserBill> userBillList = new ArrayList<>();
        //取今日00:00:00时间获得订单
        List<OrderIdsModel> orderIdsModels = orderMapper.getHadPayOrderList(DateUtil.getDaySecond0(date));

        if(!orderIdsModels.isEmpty()) {
            List<ProductStateModel> productStateModelList = productStateMapper.getProductStateList();
            Map<Integer, ProductStateModel> productStateModelMap = new HashMap<>();
            productStateModelList.forEach(productStateModel -> {
                productStateModelMap.put(productStateModel.getProductId(), productStateModel);
            });

            //计算电费剩余天数
            List<String> ids = orderIdsModels.stream().map(model -> model.getAcceptId()).collect(Collectors.toList());
            List<OrderPowerModel> orderPowerModelList = orderPowerMapper.getAcceptOrderPowerDay(ids);
            Map<String, Integer> powerDayMap = new HashMap<>();
            for (OrderPowerModel model : orderPowerModelList) {
                if (!powerDayMap.containsKey(model.getAcceptId())) {
                    powerDayMap.put(model.getAcceptId(), model.getDuration());
                } else {
                    powerDayMap.put(model.getAcceptId(), powerDayMap.get(model.getAcceptId()) + model.getDuration());
                }
            }

            //电费扣除信息
            List<PowerSpendModel> powerSpendModelList = new ArrayList<>();
            //订单到期提醒信息
            List<OrderNotifyModel> orderNotifyModelList = new ArrayList<>();
            //处理每日收益账单
            for (OrderIdsModel orderIdsModel : orderIdsModels) {
                // 检测订单商品是否暂停,enabled=0或者updateTime是昨日
                ProductStateModel pModel = productStateModelMap.get(orderIdsModel.getProductId());
                if(pModel.getEnabled().equals(0) || DateUtil.differentDays(pModel.getUpdateTime(), new Date()) == 1){
                    continue;
                }

                boolean isLastDay = false;//订单是否到期
                //账单剩余天数
                int offsetDay = DateUtil.differentDays(date, orderIdsModel.getExpireTime());
                if (offsetDay <= 0) {
                    //账单到期
                    orderIds.add(orderIdsModel.getOrderId());
                    isLastDay = true;
                }

                if(offsetDay == 7 || offsetDay == 1 || offsetDay == -1){
                    OrderNotifyModel orderNotifyModel = new OrderNotifyModel();
                    orderNotifyModel.setUserId(orderIdsModel.getPuid());
                    orderNotifyModel.setAcceptOrderId(orderIdsModel.getAcceptId());
                    orderNotifyModel.setOffsetDay(offsetDay);

                    orderNotifyModelList.add(orderNotifyModel);
                }

                //处理电费
                Integer powerDay = powerDayMap.get(orderIdsModel.getAcceptId());
                if (powerDay == null) {
                    powerDay = 0;
                    log.error("【每日收益账单】获取电费账单历史天数为0，acceptId={}", orderIdsModel.getAcceptId());
                }
                Integer curOffsetDay = powerDay;
                if (date.getTime() > orderIdsModel.getWorkTime().getTime()) {
                    Integer day1 = DateUtil.differentDays(orderIdsModel.getWorkTime(), date);
                    curOffsetDay -= day1;
                }
                //一天的电费
                Double powerPreDay = 0D;
                ProductModel productModel = null;
                if (curOffsetDay < 0) {  //电费不足
                    productModel = productService.selectOneByProductId(orderIdsModel.getProductId());
                    powerPreDay = productModel.getPowerFee() * orderIdsModel.getAccept() * orderIdsModel.getExchangeRate();
                }

                if (isLastDay) {
                    if(productModel == null) {
                        productModel = productService.selectOneByProductId(orderIdsModel.getProductId());
                    }

                    //处理没有用完的电费
                    UserBill userBill = new UserBill();
                    userBill.setId(KeyUtil.genUniqueKey());
                    userBill.setPuid(orderIdsModel.getPuid());
                    userBill.setDay(ymd);
                    userBill.setOrderId(orderIdsModel.getAcceptId());
                    //计算剩余的电费(天数)价值
                    userBill.setEarn(curOffsetDay * productModel.getPowerFee() * orderIdsModel.getAccept() * orderIdsModel.getExchangeRate());
                    userBill.setBillType(BillTypeEnum.BILL_TYPE_POWER_BACK.getCode());
                    userBill.setDiscrible("订单到期，返还电费" + curOffsetDay + "天");
                    userBill.setCreateTime(date);
                    userBillList.add(userBill);
                }

                UserBill userBill = new UserBill();
                userBill.setId(KeyUtil.genUniqueKey());
                userBill.setPuid(orderIdsModel.getPuid());
                userBill.setDay(ymd);
                userBill.setOrderId(orderIdsModel.getAcceptId());
                userBill.setAccept(orderIdsModel.getAccept());
                userBill.setEarn(orderIdsModel.getAccept() * rate - powerPreDay);
                Integer day = DateUtil.differentDays(date, orderIdsModel.getExpireTime());
                userBill.setAcceptDay((day.intValue() > orderIdsModel.getPeriod().intValue() ? orderIdsModel.getPeriod() : day));
                userBill.setPowerDay((curOffsetDay < 0 ? 0 : curOffsetDay));
                userBill.setBillType(BillTypeEnum.BILL_TYPE_DAILY.getCode());
                userBill.setCreateTime(date);
                userBill.setDiscrible("用户每日收益");
                userBillList.add(userBill);

//                if(!shareMap.containsKey(userBill.getPuid())){
//                    shareMap.put(userBill.getPuid(), userBill.getEarn());
//                }else {
//                    shareMap.put(userBill.getPuid(), shareMap.get(userBill.getPuid()) + userBill.getEarn());
//                }

                if (curOffsetDay < 0) {  //电费不足时，记录日志
                    PowerSpendModel powerSpendModel = new PowerSpendModel();
                    powerSpendModel.setBillId(userBill.getId());
                    powerSpendModel.setPowerSpend(powerPreDay);
                    powerSpendModel.setPuid(orderIdsModel.getPuid());
                    powerSpendModel.setCreateTime(date);
                    powerSpendModelList.add(powerSpendModel);
                }
            }

//            //处理返佣收益
//            if(!shareMap.isEmpty()){
//                Set<Long> uids = shareMap.keySet();
//                List<UserInvite> inviteList = inviteMapper.getRecommendUserByInviteIds(uids);
//                if(!inviteList.isEmpty()) {
//                    //返佣账单额外信息
//                    List<RecommendBillItem> recommendBillItems = new ArrayList<>();
//                    for (UserInvite invite : inviteList) {
//                        Long inviteUid = invite.getInviteUid();
//                        if(shareMap.containsKey(inviteUid)){
//                            Double reEarn = shareMap.get(inviteUid) * invite.getRate() / 100.0D;
//
//                            //返佣账单
//                            UserBill userBill = new UserBill();
//                            userBill.setId(KeyUtil.genUniqueKey());
//                            userBill.setPuid(invite.getRecommendUid());
//                            userBill.setDay(ymd);
//                            userBill.setEarn(reEarn);
//                            userBill.setBillType(BillTypeEnum.BILL_TYPE_BROKERAGE.getCode());
//                            userBill.setDiscrible("返佣");
//                            userBill.setCreateTime(date);
//                            userBillList.add(userBill);
//
//                            //账单额外信息
//                            RecommendBillItem recommendBillItem = new RecommendBillItem();
//                            recommendBillItem.setBillId(userBill.getId());
//                            recommendBillItem.setInviteUid(invite.getInviteUid());
//                            recommendBillItem.setRecommendUid(invite.getRecommendUid());
//                            recommendBillItem.setCreateTime(date);
//                            recommendBillItems.add(recommendBillItem);
//                        }
//                    }
//                    billItemMapper.inserts(recommendBillItems);
//                }
//            }

            //添加电费扣除详单
            if(!powerSpendModelList.isEmpty()){
                powerSpendMapper.inserts(powerSpendModelList);
            }

            //添加新的订单
            if (!userBillList.isEmpty()) {
                userBillService.insertBills(userBillList);
            }

            //处理每日收益账单后，处理到期订单
            if(!orderIds.isEmpty()) {
                orderMapper.updateOrderHadExpiredState(orderIds);
            }

            //异步处理到期提醒
            if(!orderNotifyModelList.isEmpty()) {
                orderNotifyTask.setNotifyModelList(orderNotifyModelList);
                executorService.execute(orderNotifyTask);
            }
        }
    }

    @Override
    @Transactional
    public void taskBalance() {
        Map<Long, Double> earnMap = new HashMap<>();
        Map<Long, Double> dailyEarnMap = new HashMap<>();
        Map<Long, Double> recommendEarnMap = new HashMap<>();
        Map<Long, Double> balanceMap = new HashMap<>();
        List<UserBill> userBillList = userBillService.getAllActivityBills();
        if(!userBillList.isEmpty()) {
            for (UserBill userBill : userBillList) {
                Long userId = userBill.getPuid();
                if (!earnMap.containsKey(userId)) {
                    earnMap.put(userId, 0D);
                }
                if (!dailyEarnMap.containsKey(userId)) {
                    dailyEarnMap.put(userId, 0D);
                }
                if (!recommendEarnMap.containsKey(userId)) {
                    recommendEarnMap.put(userId, 0D);
                }
                earnMap.put(userId, earnMap.get(userId) + userBill.getEarn());
                if (userBill.getBillType().equals(BillTypeEnum.BILL_TYPE_DAILY.getCode())) {
                    //每日收益
                    dailyEarnMap.put(userId, dailyEarnMap.get(userId) + userBill.getEarn());
                }else if(userBill.getBillType().equals(BillTypeEnum.BILL_TYPE_BROKERAGE.getCode())){
                    //返佣收益
                    recommendEarnMap.put(userId, recommendEarnMap.get(userId) + userBill.getEarn());
                }
                userBill.setEnabled(BillEnableEnum.BILL_ENABLED_HAD_SETTLEMENT.getCode());
            }

            //计入余额
            if (!earnMap.isEmpty()) {
                List<UserPayModel> userPayModelList = userPayMapper.getUserByUserIds(earnMap.keySet());
                Date date = new Date();
                for (UserPayModel userPayModel : userPayModelList) {
                    userPayModel.setBalance(userPayModel.getBalance() + earnMap.get(userPayModel.getPuid()));
                    userPayModel.setTotal(userPayModel.getTotal() + dailyEarnMap.get(userPayModel.getPuid()));
                    userPayModel.setRecommendTotal(userPayModel.getRecommendTotal() + recommendEarnMap.get(userPayModel.getPuid()));
                    userPayModel.setUpdateTime(date);
                    balanceMap.put(userPayModel.getPuid(), userPayModel.getBalance());
                }

                userPayMapper.updateUserPayBatch(userPayModelList);
            }

            for (UserBill userBill : userBillList) {
                if(balanceMap.containsKey(userBill.getPuid())){
                    userBill.setBalance(balanceMap.get(userBill.getPuid()));
                }
            }
            userBillService.updateBillStateAndBalance(userBillList);
        }
    }

    @Override
    public void checkWalletAddress() {
        List<WalletAddressModel> list = walletAddressService.getHadBalanceWalletAddressList();
        List<String> walletAddressList = new ArrayList<>();
        for(WalletAddressModel model : list){
            double balanace = btcRpcService.GetBalance(model.getWalletAddress());
            if(balanace <= 0.0D){
                walletAddressList.add(model.getWalletAddress());
            }
        }
        //设置收款地址为可用状态
        if(!walletAddressList.isEmpty()) {
            walletAddressService.updateEnabledBatch(walletAddressList, 1);
        }
    }
}
