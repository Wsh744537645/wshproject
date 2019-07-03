package com.mpool.share.mapper.order;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.product.OrderAcceptModel;
import com.mpool.share.resultVo.OrderListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;


/**
 * @Author: stephen
 * @Date: 2019/5/22 16:41
 */

@Mapper
public interface OrderAcceptMapper extends BaseMapper<OrderAcceptModel> {

    IPage<OrderListVO> getAcceptOrderListByState(IPage<OrderListVO> page, @Param("userId") Long userId, @Param("state") Integer state);

    Map<String, Object> getNormalInfo(@Param("userId") Long userId, @Param("acceptId") String acceptId);

    Map<String, Object> getOrderPayInfo(@Param("userId") Long userId, @Param("acceptId") String acceptId);

    /**
     * 获得当前总的算力
     * @param userId
     * @return
     */
    Map<String, Object> getCurAcceptTotal(@Param("userId") Long userId);
}
