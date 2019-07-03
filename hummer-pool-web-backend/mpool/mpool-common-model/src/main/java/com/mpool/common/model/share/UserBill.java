package com.mpool.common.model.share;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: stephen
 * @Date: 2019/5/28 14:48
 */

@Data
@TableName("user_bill")
public class UserBill {
    private String id;

    private Long puid;

    private String day;

    private String orderId;

    private Double earn;

    public Double balance;

    public Long accept;

    private Integer acceptDay;

    private Integer powerDay;

    private Integer billType; //1:每日收益，2:订单支付超出，3:订单支付不足，超时，4:订单到期，返还电费，5:返佣，6：管理员补币等，7：管理员打款后扣钱账单

    private Integer enabled = 0; //0：待结算，1：已结算，2：待打款，3：已打款，4：不作统计

    private String discrible;

    private String paidId;

    private Date createTime = new Date();
}
