package com.mpool.share.admin.module.bill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.share.UserBillToBePay;
import com.mpool.share.admin.model.UserBillToBePayVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/6/18 15:21
 */

@Mapper
public interface UserBillToBePaidMapper extends BaseMapper<UserBillToBePay> {

    /**
     * 获得待支付账单
     * @return
     */
    IPage<UserBillToBePayVO> getToBePaidBillList(IPage<UserBillToBePayVO> page, @Param("begTime") Date begTime, @Param("endTime") Date endTime, @Param("state") Integer state);

    /**
     * 搜索账单
     * @param paidId
     * @return
     */
    UserBillToBePayVO getToBePaidBillByPaidId(@Param("paidId") String paidId);
    UserBillToBePayVO getToBePaidBillByTxid(@Param("txid") String txid);

    void updateBillState(@Param("paidId") String paidId, @Param("txid") String txid, @Param("date") Date date, @Param("rate") Double rate);

    UserBillToBePay getBillToBePaidByTxid(@Param("txid") String txid);

    /**
     * 更新账单的支付金额
     * @param paidId
     * @param payNum
     */
    void updatePayNumByPaidId(@Param("paidId") String paidId, @Param("payNum") Double payNum);

    void deleteOne(@Param("paidId") String paidId);

    /**
     * 获得打款日志列表
     * @param page
     * @return
     */
    IPage<Map<String, Object>> getAdminPayLog(IPage<Map<String, Object>> page);
}
