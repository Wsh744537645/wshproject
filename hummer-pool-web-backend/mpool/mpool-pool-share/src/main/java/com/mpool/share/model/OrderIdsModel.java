package com.mpool.share.model;

import lombok.Data;

import java.util.Date;

/**
 * @Author: stephen
 * @Date: 2019/5/27 17:01
 */

@Data
public class OrderIdsModel {
    private Long puid;

    private String orderId;

    private String acceptId;

    private Integer productId;

    private Integer period;

    private String collectionAddress;

    private Long accept; //订单购买的算力值

    private Date expireTime; //算力订单到期时间

    private Date workTime; //开始算收益的时间

    private Double payCoin; //需要支付的货币(btc)数量

    private Double hadPay; //已经支付的数量

    private Double exchangeRate; //创建订单时的汇率
}
