package com.mpool.common.model.product;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: stephen
 * @Date: 2019/5/22 16:40
 */

@TableName("product_order_power")
@Data
public class OrderPowerModel {
    private String powerId;

    private String orderId;

    private String acceptId;

    private Integer duration;

    private Date workTime; //开始产生收益时间

    private Date expireTime;

    private Date createTime;
}
