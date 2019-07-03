package com.mpool.share.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.share.resultVo.OrderListVO;

/**
 * @Author: stephen
 * @Date: 2019/5/29 16:20
 */
public interface OrderPowerService {

    /**
     * 获得电力订单列表
     * @param page
     * @param state
     */
    IPage<OrderListVO> getPowerOrderList(Long userId, IPage<OrderListVO> page, Integer state);
}
