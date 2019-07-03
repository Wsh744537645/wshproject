package com.mpool.common.model.billenem;

/**
 * @Author: stephen
 * @Date: 2019/6/21 15:23
 */
public enum BillEnableEnum {
    BILL_ENABLED_WILL_SETTLEMENT("待结算", 0),
    BILL_ENABLED_HAD_SETTLEMENT("已结算", 1),
    BILL_ENABLED_WILL_PAY("待打款", 2),
    BILL_ENABLED_HAD_PAY("已打款", 3),
    BILL_ENABLED_OTHER("不作统计", 4);

    private String disc;
    private Integer code;

    BillEnableEnum(String disc, Integer state){
        this.disc = disc;
        this.code = state;
    }

    public Integer getCode(){
        return code;
    }
}
