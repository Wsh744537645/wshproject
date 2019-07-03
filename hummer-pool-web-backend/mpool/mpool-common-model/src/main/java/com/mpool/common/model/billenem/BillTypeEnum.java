package com.mpool.common.model.billenem;

/**
 * @Author: stephen
 * @Date: 2019/6/21 15:35
 */
public enum BillTypeEnum {
    BILL_TYPE_DAILY("每日收益",1),
    BILL_TYPE_ORDER_PAY_OVER("订单支付超出",2),
    BILL_TYPE_ORDER_PAY_BACK("订单支付不足，超时返还费用",3),
    BILL_TYPE_POWER_BACK("订单到期，返还电费",4),
    BILL_TYPE_BROKERAGE("返佣",5),
    BILL_TYPE_ADMIN_ADD_MONEY("管理员补币",6),
    BILL_TYPE_PAY_COMPLETE("管理员打款后扣余额",7);


    private String disc;
    private Integer code;

    BillTypeEnum(String disc, Integer code){
        this.disc = disc;
        this.code = code;
    }

    public Integer getCode(){
        return this.code;
    }
}
