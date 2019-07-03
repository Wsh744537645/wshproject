package com.mpool.share.admin.module.bill.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.share.UserBillToBePay;
import com.mpool.share.admin.model.UserBillToBePayVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/6/18 15:22
 */
public interface UserBillService {

    /**
     * 获取已结算账单
     * @return
     */
    IPage<Map<String, Object>> getSettlementBillList(IPage<Map<String, Object>> page, Long userId);

    /**
     * 获得待支付账单
     * @return
     */
    IPage<UserBillToBePayVO> getToBePaidBillList(IPage<UserBillToBePayVO> page, Date begTime, Date endTime, Integer state);

    /**
     * 搜索待支付账单
     * @return
     */
    UserBillToBePayVO findToBePaidBill(String paidId, String txid);

    /**
     * 获得已经支付的账单
     * @return
     */
    IPage<Map<String, Object>> getHadPaidBillList(IPage<Map<String, Object>> page);

    /**
     * 生成待支付账单
     * @param billIds
     */
    void createTobePaidBill(List<String> billIds);

    /**
     * 给用户增加btc，新增支付账单
     * @param username
     * @param payNum
     * @param discrible
     */
    void createExtraBill(String username, Double payNum, String discrible);

    /**
     * 完成打款，更新账单状态
     * @param paidId
     * @param txid
     */
    void updateBillStateByTxid(String paidId, String txid);

    /**
     * 获得待支付账单下的所有账单列表
     * @param paidId
     * @return
     */
    List<Map<String, Object>> getBillItems(String paidId);

    /**
     * 撤销待支付账单
     * @param billId
     */
    void canalToPayBill(String billId);

    /**
     * 获得邀请返佣收益总和
     * @return
     */
    Double getRecommendShareTotal();

    /**
     * 获得管理员打款总和以及算力总和
     * @return
     */
    Map<String, Object> getAdminPay();

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
     * 获取管理员对于算力订单打款明细列表
     * @param page
     * @param begTime
     * @param endTime
     * @param productName
     * @return
     */
    IPage<Map<String, Object>> getAcceptOrderAdminPayList(IPage<Map<String, Object>> page, @Param("begTime") Date begTime, @Param("endTime") Date endTime, @Param("productName") String productName);

    /**
     * 获得打款日志列表
     * @param page
     * @return
     */
    IPage<Map<String, Object>> getAdminPayLog(IPage<Map<String, Object>> page);
}
