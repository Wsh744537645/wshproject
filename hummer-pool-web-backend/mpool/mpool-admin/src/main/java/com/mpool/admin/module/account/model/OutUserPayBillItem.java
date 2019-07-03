package com.mpool.admin.module.account.model;

public class OutUserPayBillItem {
    //需要新增的金额
    private Double payBtc;
    //需要新增的金额的类型描述
    private String desc;

    private String username;

    public Double getPayBtc() {
        return payBtc;
    }

    public void setPayBtc(Double payBtc) {
        this.payBtc = payBtc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
