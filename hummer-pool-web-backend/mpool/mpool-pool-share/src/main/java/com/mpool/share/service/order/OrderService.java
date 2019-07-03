package com.mpool.share.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.share.model.OrderCreateModel;
import com.mpool.share.resultVo.OrderCreateVO;
import com.mpool.share.resultVo.OrderListVO;

/**
 * @Author: stephen
 * @Date: 2019/5/23 11:59
 */
public interface OrderService {

    /**
     * 创建订单
     * @param model
     * @return
     */
    OrderCreateVO createOrder(OrderCreateModel model);

    /**
     * 处理支付过期订单，把过期订单占有的算力还给库存
     */
    boolean checkExpiredOrder();

    /**
     * 检测待支付的订单是否已经完成支付
     */
    void checkHadPayOrder();
}
