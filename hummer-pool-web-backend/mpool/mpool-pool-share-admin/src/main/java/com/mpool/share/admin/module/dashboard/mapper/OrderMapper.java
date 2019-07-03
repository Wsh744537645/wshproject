package com.mpool.share.admin.module.dashboard.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.product.OrderModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/5/22 16:32
 */

@Mapper
public interface OrderMapper {

    /**
     * 获得订单收入总和
     * @return
     */
    Double getTotalPay();

    /**
     * 获得电费收入总和
     * @return
     */
    Double getTotalPowerPay();

    /**
     * 订单收入明细
     * @param page
     * @param productName
     * @param orderId
     * @param userName
     * @return
     */
    IPage<Map<String, Object>> getHadPayOrderList(IPage<Map<String, Object>> page, @Param("productName") String productName, @Param("orderId") String orderId, @Param("userName") String userName);

    /**
     * 电费收入明细
     * @param page
     * @param productName
     * @param orderId
     * @param userName
     * @return
     */
    IPage<Map<String, Object>> getHadPayPowerOrderList(IPage<Map<String, Object>> page, @Param("productName") String productName, @Param("orderId") String orderId, @Param("userName") String userName);

    /**
     * 通过产品ID获得订单Id列表
     * @param productIds
     * @return
     */
    List<Map<String, Object>> getOrderIdsListByProductId(@Param("productIds") List<Integer> productIds);

    /**
     * 增加订单的到期时间
     * @param orderIds
     * @param second
     */
    void addOrderExpireTime(@Param("orderIds") List<String> orderIds, @Param("second") Integer second);

    void updateOrderExpiredTime(@Param("orderModels") List<OrderModel> orderModels);
}
