package com.mpool.share.mapper.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.product.OrderPowerModel;
import com.mpool.share.resultVo.OrderListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/22 16:42
 */

@Mapper
public interface OrderPowerMapper extends BaseMapper<OrderPowerModel> {

    /**
     * 根据算力订单id获得电费天数总和
     * @param acceptIds
     * @return
     */
    List<OrderPowerModel> getAcceptOrderPowerDay(@Param("acceptIds") List<String> acceptIds);

    IPage<OrderListVO> getPowerOrderListByState(IPage<OrderListVO> page, @Param("userId") Long userId, @Param("state") Integer state);
}
