package com.mpool.share.model;

import lombok.Data;

/**
 * @Author: stephen
 * @Date: 2019/6/5 15:15
 */

@Data
public class OrderNotifyModel {
    private Long userId;

    private String acceptOrderId;

    private Integer offsetDay;
}
