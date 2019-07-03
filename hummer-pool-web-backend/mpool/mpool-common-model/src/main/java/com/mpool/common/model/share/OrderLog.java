package com.mpool.common.model.share;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: stephen
 * @Date: 2019/5/29 15:40
 */

@Data
@TableName("log_order")
public class OrderLog {
    private String orderId;

    private Double coin;

    private String discrible;

    private Date createTime;
}
