package com.mpool.common.model.product;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: stephen
 * @Date: 2019/5/22 16:38
 */

@TableName("product_order_accept")
@Data
public class OrderAcceptModel {
    private String acceptId;

    private String orderId;

    private Integer productId;

    private Long accept;

    private Date workTime; //开始产生收益时间

    private Date expireTime;

    private Date createTime;
}
