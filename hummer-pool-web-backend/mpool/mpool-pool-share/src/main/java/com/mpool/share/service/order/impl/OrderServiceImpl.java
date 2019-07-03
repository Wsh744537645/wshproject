package com.mpool.share.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpool.common.CoinValidationUtil;
import com.mpool.common.model.billenem.BillTypeEnum;
import com.mpool.common.model.product.OrderAcceptModel;
import com.mpool.common.model.product.OrderModel;
import com.mpool.common.model.product.OrderPowerModel;
import com.mpool.common.model.product.WalletAddressModel;
import com.mpool.common.model.share.*;
import com.mpool.common.util.DateUtil;
import com.mpool.share.exception.ExceptionCode;
import com.mpool.share.exception.ShareException;
import com.mpool.share.mapper.order.OrderLogMapper;
import com.mpool.share.mapper.UserPayMapper;
import com.mpool.share.mapper.order.OrderAcceptMapper;
import com.mpool.share.mapper.order.OrderMapper;
import com.mpool.share.mapper.order.OrderPowerMapper;
import com.mpool.share.model.*;
import com.mpool.share.resultVo.OrderCreateVO;
import com.mpool.share.service.*;
import com.mpool.share.service.order.OrderService;
import com.mpool.share.service.product.ProductService;
import com.mpool.share.utils.AccountSecurityUtils;
import com.mpool.share.utils.DataUtils;
import com.mpool.share.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: stephen
 * @Date: 2019/5/23 12:00
 */

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ProductService productService;
    @Autowired
    private RealTimeDataService realTimeDataService;
    @Autowired
    private WalletAddressService walletAddressService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderAcceptMapper orderAcceptMapper;
    @Autowired
    private OrderPowerMapper orderPowerMapper;
    @Autowired
    private BtcRpcService btcRpcService;
    @Autowired
    private UserBillService userBillService;
    @Autowired
    private OrderLogMapper orderLogMapper;
    @Autowired
    private UserPayMapper userPayMapper;
    @Autowired
    private ShareService shareService;
    @Autowired
    private RecommendService recommendService;

    private static Lock createOrderLock = new ReentrantLock();

    @Override
    @Transactional
    public OrderCreateVO createOrder(OrderCreateModel model) {
        createOrderLock.lock();
        try {
            ProductModel productModel = productService.selectOneByProductId(model.getProductId());
            if(productModel == null){
                log.error("【创建订单失败】 商品不存在，productId={}", model.getProductId());
                //商品不存在
                throw new ShareException(ExceptionCode.product_not_exist);
            }
            if(productModel.getStock() - model.getAccept() <= 0){
                //检测是否有过期订单
                if(checkExpiredOrder()){
                    productModel = productService.selectOneByProductId(model.getProductId());
                }
                if(productModel.getStock() - model.getAccept() <= 0) {
                    log.error("【创建订单失败】 库存不足，当前购买的算力：{}， 库存算力：{}", model.getAccept(), productModel.getStock());
                    //商品库存不足
                    throw new ShareException(ExceptionCode.product_stock_not_enough);
                }
            }

            Double usdRate = realTimeDataService.getCurUsdToCurrency(model.getPayType());
            if(usdRate.doubleValue() <= 0){
                log.error("【创建订单失败】 汇率为0");
                throw new ShareException(ExceptionCode.btc_2_usd_zero);
            }

            WalletAddressModel walletAddressModel = walletAddressService.getValidWalletAddress();
            //MiningModel miningModel = productService.getMiningById(productModel.getMiningId());

            Date date = new Date();
            //生成总订单
            OrderModel orderModel = new OrderModel();
            orderModel.setOrderId(KeyUtil.genUniqueKey());
            orderModel.setPuid(AccountSecurityUtils.getUser().getUserId());
            orderModel.setWalletAddress(model.getWalleyAddress());
            orderModel.setCollectionAddress(walletAddressModel.getWalletAddress());
            orderModel.setState(OrderStateEnum.waitting.getState());//设置等待付款
            orderModel.setCreateTime(date);
            orderModel.setExpireTime(DateUtil.addMinute(date, DataUtils.ORDER_EFFECT_TIME_MIN));
            orderModel.setPayId(model.getPayType());
            orderModel.setExchangeRate(usdRate);
            Double coin = (model.getAccept() * productModel.getAcceptFee() * productModel.getPeriod() + model.getPowerDay()* model.getAccept() * productModel.getPowerFee()) * usdRate;
            orderModel.setPayCoin(coin);
            orderModel.setHadPay(0D);
            orderMapper.insert(orderModel);

            //生成算力订单
            OrderAcceptModel orderAcceptModel = new OrderAcceptModel();
            orderAcceptModel.setAcceptId(KeyUtil.genAccetpKey());
            orderAcceptModel.setOrderId(orderModel.getOrderId());
            orderAcceptModel.setProductId(model.getProductId());
            orderAcceptModel.setAccept(model.getAccept());
            orderAcceptModel.setCreateTime(date);
            orderAcceptMapper.insert(orderAcceptModel);

            //生成电费订单
            OrderPowerModel orderPowerModel = new OrderPowerModel();
            orderPowerModel.setPowerId(KeyUtil.genPowerKey());
            orderPowerModel.setOrderId(orderModel.getOrderId());
            orderPowerModel.setAcceptId(orderAcceptModel.getAcceptId());
            orderPowerModel.setDuration(model.getPowerDay());
            orderPowerModel.setCreateTime(date);
            orderPowerMapper.insert(orderPowerModel);

            //扣库存
            productModel.setStock(productModel.getStock() - model.getAccept());
            productService.update(productModel);

            //保存用户钱包地址
            if(model.getWalleyAddress() != null && !model.getWalleyAddress().isEmpty()){
                UserPayModel userPayModel = new UserPayModel();
                userPayModel.setPuid(AccountSecurityUtils.getUser().getUserId());
                userPayModel = userPayMapper.selectOne(new QueryWrapper<>(userPayModel));
                if(userPayModel != null && (userPayModel.getWalletAddress() == null || userPayModel.getWalletAddress().isEmpty())){
                    //检查钱包地址合法性
                    boolean bitCoinAddressValidate = CoinValidationUtil.bitCoinAddressValidate(model.getWalleyAddress());
                    if(!bitCoinAddressValidate){
                        throw new ShareException(ExceptionCode.wallet_address_error);
                    }

                    userPayMapper.updateWalletAddress(userPayModel.getPuid(), model.getWalleyAddress());
                }
            }

            return new OrderCreateVO(orderModel, orderAcceptModel, orderPowerModel, productModel);
        }finally {
            createOrderLock.unlock();
        }
    }

    @Override
    @Transactional
    public boolean checkExpiredOrder() {
        Date date = new Date();
        String ymd = DateUtil.getYYYYMMdd(date);
        List<Map<String, Object>> list = orderMapper.getExpiredOrderAccept(date);
        if(!list.isEmpty()){
            List<String> orderIdList = new ArrayList<>();
            //没有被支付过的钱包
            List<String> walletAddressList_no = new ArrayList<>();
            //被支付过的钱包
            List<String> walletAddressList_had = new ArrayList<>();
            Map<Integer, Long> map = new HashMap<>();
            List<UserBill> userBillList = new ArrayList<>();
            for(Map<String, Object> tmp : list){
                orderIdList.add(tmp.get("order_id").toString());

                Integer productId = Integer.parseInt(tmp.get("product_id").toString());
                Long accept = Long.parseLong(tmp.get("accept").toString());
                if(map.containsKey(productId)){
                    map.put(productId, map.get(productId) + accept);
                }else{
                    map.put(productId, accept);
                }

                //查看该订单是否支付过，如果支付过需要生成收益账单，把余额归还用户
                Double hadPay = Double.parseDouble(tmp.get("had_pay").toString());
                if(hadPay.doubleValue() > 0.0D){
                    UserBill userBill = new UserBill();
                    userBill.setId(KeyUtil.genUniqueKey());
                    userBill.setPuid(Long.parseLong(map.get("puid").toString()));
                    userBill.setDay(ymd);
                    userBill.setOrderId(tmp.get("acceptId").toString());
                    userBill.setEarn(hadPay);
                    userBill.setBillType(BillTypeEnum.BILL_TYPE_ORDER_PAY_BACK.getCode());
                    userBill.setDiscrible("用户支付了订单一部分金额，但超出了支付时间，把金额返还用户");
                    userBillList.add(userBill);

                    walletAddressList_had.add(tmp.get("collection_address").toString());
                }
                else{
                    walletAddressList_no.add(tmp.get("collection_address").toString());
                }
            }

            if(!walletAddressList_no.isEmpty()) {
                walletAddressService.updateEnabledBatch(walletAddressList_no, 1);
            }
            if(!walletAddressList_had.isEmpty()) {
                walletAddressService.updateEnabledBatch(walletAddressList_had, 2);
            }
            productService.addAcceptStockBatch(map);
            orderMapper.updateOrderStateBatch(orderIdList, 4);
//            orderMapper.deleteAllExpiredOrderPower(orderIdList);
//            orderMapper.deleteAllExpiredOrderAccept(orderIdList);
//            orderMapper.deleteAllExpiredOrder(orderIdList);

            if(!userBillList.isEmpty()){
                userBillService.insertBills(userBillList);
                shareService.taskBalance();
            }
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void checkHadPayOrder() {
        List<OrderIdsModel> orderModels = orderMapper.getWillPayOrderList();

        Date date = new Date();
        String ymd = DateUtil.getYYYYMMdd(date);
        List<String> orderIds = new ArrayList<>();
        List<OrderAcceptModel> orderAcceptModels = new ArrayList<>();
        List<OrderIdsModel> orderUpdateList = new ArrayList<>();
        List<UserBill> userBillList = new ArrayList<>();

        //记录支付完成的用户信息
        Map<Integer, ProductModel> productModelMap = new HashMap<>();
        if(!orderModels.isEmpty()){
            Map<Long, Double> shareMap = new HashMap<>();
            List<String> walletAddressList = new ArrayList<>();
            List<OrderLog> orderLogList = new ArrayList<>();
            for(OrderIdsModel model : orderModels) {
                double balanace = btcRpcService.GetBalance(model.getCollectionAddress());
                if(balanace > 0.0D){
                    //log
                    OrderLog orderLog = new OrderLog();
                    orderLog.setOrderId(model.getOrderId());
                    orderLog.setCoin(balanace);
                    orderLog.setCreateTime(date);
                    String dis = "uid:" + model.getPuid() + " ，该订单需要支付：" + model.getPayCoin() + " ，当前已支付：" + (model.getHadPay() + balanace);
                    orderLog.setDiscrible(dis);
                    orderLogList.add(orderLog);

                    model.setHadPay(balanace);
                    if(model.getPayCoin() <= balanace) {
                        orderIds.add(model.getOrderId());
                        orderUpdateList.add(model);
                        walletAddressList.add(model.getCollectionAddress());

                        ProductModel productModel;
                        if (!productModelMap.containsKey(model.getProductId())) {
                            productModel = productService.selectOneByProductId(model.getProductId());
                            productModelMap.put(model.getProductId(), productModel);
                        } else {
                            productModel = productModelMap.get(model.getProductId());
                        }

                        Date curdate = new Date();
                        if(curdate.getTime() < productModel.getWorkTime().getTime()){
                            curdate = productModel.getWorkTime();
                        }else{
                            //如果是在开始挖矿时间当天购买的，用户的订单设置从第二天开始生效
                            if(DateUtil.getYYYYMMdd(curdate).equals(DateUtil.getYYYYMMdd(productModel.getWorkTime()))){
                                curdate = DateUtil.addDay(curdate, 1);
                            }
                        }

                        Date expiredTime = DateUtil.addDay(curdate, productModel.getPeriod());
                        OrderAcceptModel orderAcceptModel = new OrderAcceptModel();
                        orderAcceptModel.setAcceptId(model.getAcceptId());
                        orderAcceptModel.setOrderId(model.getOrderId());
                        orderAcceptModel.setProductId(model.getProductId());
                        orderAcceptModel.setWorkTime(curdate);
                        orderAcceptModel.setExpireTime(expiredTime);
                        orderAcceptModels.add(orderAcceptModel);

                        if(balanace > model.getPayCoin()){ //支付超出了
                            UserBill userBill = new UserBill();
                            userBill.setId(KeyUtil.genUniqueKey());
                            userBill.setPuid(model.getPuid());
                            userBill.setDay(ymd);
                            userBill.setOrderId(model.getAcceptId());
                            userBill.setEarn(balanace - model.getPayCoin());
                            userBill.setBillType(BillTypeEnum.BILL_TYPE_ORDER_PAY_OVER.getCode());
                            userBill.setDiscrible("用户支付订单超出支付金额，把多余的返还用户");
                            userBillList.add(userBill);
                        }

                        if(!shareMap.containsKey(model.getPuid())){
                            shareMap.put(model.getPuid(), model.getPayCoin());
                        }else {
                            shareMap.put(model.getPuid(), shareMap.get(model.getPuid()) + model.getPayCoin());
                        }
                    }else{ //支付不足
                        model.setExpireTime(DateUtil.addMinute(model.getExpireTime(), DataUtils.ORDER_PAY_TIME_MIN));
                        orderUpdateList.add(model);
                    }
                }
            }

            //处理返佣收益
            recommendService.setRecommendShare(shareMap);

            //查询付款日志
            if(!orderLogList.isEmpty()) {
                orderLogMapper.inserts(orderLogList);
            }

            //设置收款地址可以把余额转出
            if(!walletAddressList.isEmpty()) {
                walletAddressService.updateEnabledBatch(walletAddressList, 2);
            }

            if(!orderIds.isEmpty()){
                orderMapper.updateOrderStateBatch(orderIds, 0);
                orderMapper.updateOrderAcceptExpiredTime(orderAcceptModels);
            }

            if(!orderUpdateList.isEmpty()){
                orderMapper.updateOrderWillPayState(orderUpdateList, 2);
            }

            if(!userBillList.isEmpty()){
                userBillService.insertBills(userBillList);
                shareService.taskBalance();
            }
        }
    }

}
