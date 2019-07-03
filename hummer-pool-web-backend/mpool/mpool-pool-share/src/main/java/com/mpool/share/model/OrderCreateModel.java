package com.mpool.share.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author: stephen
 * @Date: 2019/5/23 11:37
 */

@Data
public class OrderCreateModel {
    /**
     * 产品id
     */
    @NotNull
    private Integer productId;

    /**
     * 购买的算力
     */
    @NotNull
    private Long accept;

    /**
     * 电费缴纳天数
     */
    @NotNull
    private Integer powerDay;

    /**
     * 钱包地址
     */
    @NotEmpty
    private String walleyAddress;

    /**
     * 支付方式，默认btc
     */
    private Integer payType;

    public Integer getPayType() {
        if(payType == null){
            return 1;
        }
        return payType;
    }
}
