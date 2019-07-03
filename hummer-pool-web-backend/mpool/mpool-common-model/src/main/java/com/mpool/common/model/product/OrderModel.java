package com.mpool.common.model.product;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: stephen
 * @Date: 2019/5/22 16:33
 */

@TableName("product_order")
@Data
public class OrderModel {
    private String orderId;

    private Long puid;

    private String walletAddress; //用户收益钱包地址

    private String collectionAddress; //收款地址

    private Integer state; //0：已付款，1：等待付款，2：等待完成付款，3：已过期

    private Date createTime;

    private Date expireTime; //支付过期时间

    private Integer payId; //支付方式

    private Double exchangeRate; //创建订单时的汇率

    private Double payCoin; //需要支付的货币(btc)数量

    private Double hadPay; //已经支付的数量


    private Integer productId;
}
