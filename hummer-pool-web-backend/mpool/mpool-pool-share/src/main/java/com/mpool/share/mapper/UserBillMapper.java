package com.mpool.share.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.share.UserBill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/5/28 14:52
 */

@Mapper
public interface UserBillMapper extends BaseMapper<UserBill> {

    void insertBills(@Param("userBills") List<UserBill> userBills);

    /**
     * 获得所有没有处理过的账单
     * @return
     */
    List<UserBill> getAllActivityBills();

    /**
     * 把账单的状态置位已处理
     */
    void setBillEnabledState(@Param("userBills") List<UserBill> userBills);

    /**
     * 获取某个订单的所有产出账单
     * @param orderId
     * @return
     */
    List<UserBill> getBillByOrderId(@Param("orderId") String orderId);

    /**
     * 更新账单的state和balance
     * @param userBills
     */
    void updateBillStateAndBalance(@Param("userBills") List<UserBill> userBills);

    /**
     * 获得时间范围内用户的每日产出
     * @param userId
     * @param begTime
     * @param endTime
     * @return
     */
    IPage<Map<String, Object>> getDailyAcceptOrderList(IPage<Map<String, Object>> page,@Param("userId") Long userId, @Param("begTime") Long begTime, @Param("endTime") Long endTime);

    List<Map<String, Object>> getDailyBalance(@Param("userId") Long userId, @Param("begTime") Long begTime, @Param("endTime") Long endTime);

    /**
     * 通过算力订单获得账单列表
     * @param page
     * @param userId
     * @param orderId
     * @return
     */
    IPage<Map<String, Object>> getBillListByOrderid(IPage<Map<String, Object>> page, @Param("state") Integer state, @Param("userId")Long userId, @Param("orderId") String orderId);
}
