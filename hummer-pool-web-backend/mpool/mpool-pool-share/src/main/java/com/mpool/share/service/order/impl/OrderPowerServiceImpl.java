package com.mpool.share.service.order.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.share.mapper.order.OrderPowerMapper;
import com.mpool.share.resultVo.OrderListVO;
import com.mpool.share.service.order.OrderPowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: stephen
 * @Date: 2019/5/29 16:20
 */

@Service
public class OrderPowerServiceImpl implements OrderPowerService {
    @Autowired
    private OrderPowerMapper orderPowerMapper;

    @Override
    public IPage<OrderListVO> getPowerOrderList(Long userId, IPage<OrderListVO> page, Integer state) {
        return orderPowerMapper.getPowerOrderListByState(page, userId, state);
    }
}
