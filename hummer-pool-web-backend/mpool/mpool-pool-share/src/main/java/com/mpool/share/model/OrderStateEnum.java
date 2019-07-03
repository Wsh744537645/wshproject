package com.mpool.share.model;

/**
 * @Author: stephen
 * @Date: 2019/6/1 12:15
 */
public enum OrderStateEnum {
    activity(0, "生效中"),
    waitting(1, "等待支付"),
    waittingPay(2, "等待全额支付"),
    expired(3, "已过期"),
    canal(4, "已取消");

    private Integer state;
    private String name;

    OrderStateEnum(Integer state, String name){
        this.state = state;
        this.name = name;
    }

    public Integer getState(){
        return this.state;
    }
}
