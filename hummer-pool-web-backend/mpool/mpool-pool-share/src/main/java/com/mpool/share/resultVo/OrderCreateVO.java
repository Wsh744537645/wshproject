package com.mpool.share.resultVo;

import com.mpool.common.model.product.OrderAcceptModel;
import com.mpool.common.model.product.OrderModel;
import com.mpool.common.model.product.OrderPowerModel;
import com.mpool.common.model.share.MiningModel;
import com.mpool.common.model.share.ProductModel;
import lombok.Data;

import java.util.Date;

/**
 * @Author: stephen
 * @Date: 2019/5/23 11:57
 */

@Data
public class OrderCreateVO {
    private String orderId; //订单号

    private String walletAddress; //用户钱包地址

    private String collectionAddress; //收款地址

    private Date createTime; //下单时间

    private Date expireTime; //过期时间

    private Double exchangeRate; //创建订单时的汇率

    private Long accept; //购买的算力

    private Integer powerDay; //购买的电天数

    private String miningName; //矿机型号

    private Double acceptFee; //算力每TH/s的美元价格

    private Double powerFee; //电费:每1算力的美元价格

    private Double payCoin; //需要支付的货币(btc)数量

    private Double hadPay; //已经支付的数量

    private Integer state; //订单状态(0：已付款，1：等待付款，2：等待完成付款，3：已到期)

    public OrderCreateVO(){

    }

    public OrderCreateVO(OrderModel orderModel, OrderAcceptModel orderAcceptModel, OrderPowerModel orderPowerModel, ProductModel productModel){
        this.orderId = orderModel.getOrderId();
        this.walletAddress = orderModel.getWalletAddress();
        this.collectionAddress = orderModel.getCollectionAddress();
        this.createTime = orderModel.getCreateTime();
        this.expireTime = orderModel.getExpireTime();
        this.exchangeRate = orderModel.getExchangeRate();
        this.accept = orderAcceptModel.getAccept();
        this.powerDay = orderPowerModel.getDuration();
        //this.miningName = miningModel.getMiningName();
        this.acceptFee = productModel.getAcceptFee();
        this.powerFee = productModel.getPowerFee();
        this.payCoin = orderModel.getPayCoin();
        this.hadPay = orderModel.getHadPay();
        this.state = orderModel.getState();
    }
}
