package com.mpool.share.admin.module.dashboard.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.share.admin.module.dashboard.mapper.OrderMapper;
import com.mpool.share.admin.module.dashboard.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/6/25 16:52
 */

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Double getTotalPay() {
        return orderMapper.getTotalPay();
    }

    @Override
    public Double getTotalPowerPay() {
        return orderMapper.getTotalPowerPay();
    }

    @Override
    public IPage<Map<String, Object>> getHadPayOrderList(IPage<Map<String, Object>> page, String productName, String orderId, String userName) {
        return orderMapper.getHadPayOrderList(page, productName, orderId, userName);
    }

    @Override
    public IPage<Map<String, Object>> getHadPayPowerOrderList(IPage<Map<String, Object>> page, String productName, String orderId, String userName) {
        return orderMapper.getHadPayPowerOrderList(page, productName, orderId, userName);
    }
}
