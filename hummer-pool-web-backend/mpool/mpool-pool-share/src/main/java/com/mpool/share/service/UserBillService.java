package com.mpool.share.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.share.UserBill;

import java.util.List;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/5/28 14:54
 */
public interface UserBillService {

    /**
     * 批量增加账单
     * @param userBills
     */
    void insertBills(List<UserBill> userBills);

    /**
     * 获得所有没有处理过的账单
     * @return
     */
    List<UserBill> getAllActivityBills();

    /**
     * 把账单的状态置位已处理
     */
    void setBillEnabledState(List<UserBill> userBills);

    /**
     * 更新账单的state和balance
     * @param userBills
     */
    void updateBillStateAndBalance(List<UserBill> userBills);

    /**
     * 获得时间范围内的产出
     * @param page
     * @param userId
     * @param begTime
     * @param endTime
     * @return
     */
    IPage<Map<String, Object>> getDailyAcceptOrderList(IPage<Map<String, Object>> page, Long userId, Long begTime, Long endTime);

    /**
     * 通过算力订单获得账单列表
     * @param page
     * @param userId
     * @param orderId
     * @return
     */
    IPage<Map<String, Object>> getBillListByOrderid(IPage<Map<String, Object>> page, Integer state, Long userId, String orderId);
}
