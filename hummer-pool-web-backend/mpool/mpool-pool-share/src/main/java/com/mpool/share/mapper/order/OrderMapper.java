package com.mpool.share.mapper.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.product.OrderAcceptModel;
import com.mpool.common.model.product.OrderModel;
import com.mpool.share.model.OrderIdsModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/5/22 16:32
 */

@Mapper
public interface OrderMapper extends BaseMapper<OrderModel> {

    /**
     * 获得支付过期订单
     * @return
     */
    List<Map<String, Object>> getExpiredOrderAccept(@Param("date") Date date);

    /**
     * 删除支付过期订单
     * @param orderIdList
     */
    void deleteAllExpiredOrder(@Param("orderIdList") List<String> orderIdList);

    void deleteAllExpiredOrderAccept(@Param("orderIdList") List<String> orderIdList);

    void deleteAllExpiredOrderPower(@Param("orderIdList") List<String> orderIdList);


    /**
     * 获取所有待支付订单
     * @return
     */
    List<OrderIdsModel> getWillPayOrderList();

    /**
     * 获取所有已经支付且未到期的订单
     * @return
     */
    List<OrderIdsModel> getHadPayOrderList(@Param("date") Date date);

    /**
     * 修改总订单的支付状态
     * @param orderIds
     * @param state
     */
    void updateOrderStateBatch(@Param("orderIds") List<String> orderIds, @Param("state") Integer state);

    /**
     * 更新算力订单的开始生效时间和过期时间
     * @param orderAcceptModels
     */
    void updateOrderAcceptExpiredTime(@Param("orderAcceptModels") List<OrderAcceptModel> orderAcceptModels);

    /**
     * 修改总订单的支付状态为待支付
     * @param orderModels
     */
    void updateOrderWillPayState(@Param("orderModels") List<OrderIdsModel> orderModels, @Param("state") int state);

    /**
     * 修改到期总订单的状态
     * @param orderIds
     */
    void updateOrderHadExpiredState(@Param("orderIds") List<String> orderIds);
}
