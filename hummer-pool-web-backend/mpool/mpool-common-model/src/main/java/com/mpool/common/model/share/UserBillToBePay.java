package com.mpool.common.model.share;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: stephen
 * @Date: 2019/6/18 15:15
 */

@Data
@TableName("user_bill_tobe_pay")
public class UserBillToBePay {
    private String paidId;

    private Double payNum;

    private Integer state;

    private String txid;

    private String adminName;

    private Double exchangeRate;

    private Date payAt;

    private Date createTime;
}
