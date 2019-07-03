package com.mpool.share.admin.module.dashboard.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/6/25 16:50
 */
public interface OrderService {

    /**
     * 获得订单总收入（美元）
     * @return
     */
    Double getTotalPay();

    /**
     * 获得电费收入总和（美元）
     * @return
     */
    Double getTotalPowerPay();

    IPage<Map<String, Object>> getHadPayOrderList(IPage<Map<String, Object>> page, @Param("productName") String productName, @Param("orderId") String orderId, @Param("userName") String userName);

    IPage<Map<String, Object>> getHadPayPowerOrderList(IPage<Map<String, Object>> page, @Param("productName") String productName, @Param("orderId") String orderId, @Param("userName") String userName);
}
