package com.mpool.share.admin.module.bill.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.share.UserBill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/5/28 14:52
 */

@Mapper
public interface UserBillMapper extends BaseMapper<UserBill> {

    int insert(@Param("bill") UserBill bill);

    void insertBills(@Param("userBills") List<UserBill> userBills);

    /**
     * 获取已结算账单
     * @return
     */
    IPage<Map<String, Object>> getSettlementBillList(IPage<Map<String, Object>> page, @Param("userId") Long userId);

    /**
     * 获得已经支付的账单
     * @return
     */
    IPage<Map<String, Object>> getHadPaidBillList(IPage<Map<String, Object>> page);

    /**
     * 更新账单被打包到的待支付单号
     * @param billIds
     * @param paidId
     */
    void updateBillPaidId(@Param("billIds") List<String> billIds, @Param("paidId") String paidId);

    /**
     * 通过paidId获得账单列表
     * @param paidId
     * @return
     */
    List<UserBill> getBillListByPaidId(@Param("paidId") String paidId);

    /**
     * 通过paidId获得账单详细信息列表
     * @param paidId
     * @return
     */
    List<Map<String, Object>> getBillDetailsListByPaidId(@Param("paidId") String paidId);

    /**
     * 更新账单的enabled状态
     * @param bills
     * @param enabled
     */
    void updateBillState(@Param("bills") List<UserBill> bills, @Param("enabled") Integer enabled);

    /**
     * 获得批量账单的总收益金额
     * @param billIds
     * @return
     */
    Double getBillEarnTotal(@Param("billIds") List<String> billIds);

    /**
     * 获得订单的paidId
     * @param billId
     * @return
     */
    UserBill getPaidIdByBillId(@Param("billId") String billId);

    /**
     * 撤销账单的paidId
     * @param billId
     */
    void canalBillPaidId(@Param("billId") String billId);

    /**
     * 获得邀请返佣收益总和
     * @return
     */
    Double getRecommendShareTotal();

    /**
     * 获得返佣金额明细列表
     * @param page
     * @param begTime
     * @param endTime
     * @param username
     * @return
     */
    IPage<Map<String, Object>> getRecommendList(IPage<Map<String, Object>> page, @Param("begTime") Date begTime, @Param("endTime") Date endTime, @Param("username") String username);

    /**
     * 获得管理员对于算力订单打款总和以及算力总和
     * @return
     */
    Map<String, Object> getAcceptOrderAdminPay();

    /**
     * 获取管理员对于算力订单打款明细列表
     * @param page
     * @param begTime
     * @param endTime
     * @param productName
     * @return
     */
    IPage<Map<String, Object>> getAcceptOrderAdminPayList(IPage<Map<String, Object>> page, @Param("begTime") Date begTime, @Param("endTime") Date endTime, @Param("productName") String productName);
}
