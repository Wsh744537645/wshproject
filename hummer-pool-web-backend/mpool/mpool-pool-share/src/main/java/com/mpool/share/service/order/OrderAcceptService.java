package com.mpool.share.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.share.resultVo.OrderListVO;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/5/29 16:19
 */
public interface OrderAcceptService {
    /**
     * 获得算力订单列表
     * @param page
     * @param state
     */
    IPage<OrderListVO> getAcceptOrderList(Long userId, IPage<OrderListVO> page, Integer state);

    /**
     * 算力订单基本信息
     * @param userId
     * @param acceptId
     * @return
     */
    Map<String, Object> getAcceptNormalByOrderId(Long userId, String acceptId);

    /**
     * 算力订单付款信息
     * @param userId
     * @param acceptId
     * @return
     */
    Map<String, Object> getOrderPayInfo(Long userId, String acceptId);

    Map<String, Object> getOrderShareInfo(Long userId, String acceptId);

    /**
     * 获得当前总的算力
     * @param userId
     * @return
     */
    Map<String, Object> getCurAcceptTotal(@Param("userId") Long userId);

}
