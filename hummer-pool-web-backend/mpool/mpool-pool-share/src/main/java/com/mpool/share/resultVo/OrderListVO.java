package com.mpool.share.resultVo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: stephen
 * @Date: 2019/5/29 14:17
 */

@Data
public class OrderListVO {
    private String orderId;

    private String id;

    @JsonIgnore
    private Date createTime;

    private String currencyName;

    private Integer period;

    private Double payCoin; //需要支付的货币(btc)数量

    private Double hadPay; //已经支付的数量

    private Integer state;

    @JsonProperty("createTime")
    public Long getCreateTime(){
        return createTime.getTime() / 1000;
    }
}
