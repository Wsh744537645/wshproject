package com.mpool.admin.mapperUtils.bill.mapperBase;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.account.bill.UserPayBill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/5/13 20:40
 */
public interface UserPayBillBaseMapper<T> extends BaseMapper<T> {
    IPage<Map<String, Object>> getBillInfo(IPage<Map<String, Object>> iPage, @Param("param") Map<String, Object> param);
//	List<UserPayBill> getBillInfoBtcSum(@Param("param") Map<String, Object> param);

    /**
     * wgg 根据单号或TXID搜索 支付信息
     *
     * @param billNum
     * @return
     */
    UserPayBill getBillInfoByNumOrTxid(@Param("billNum") String billNum, @Param("txid") String txid);

    /**
     * 通过时间段搜索获得billinfo
     *
     * @param strTime endTime
     * @param iPage
     * @return
     */
    IPage<Map<String, Object>> getBillInfoByDate(IPage<Map<String, Object>> iPage, @Param("strTime") Date strTime,
                                                 @Param("endTime") Date endTime);

    List<UserPayBill> exportBillList(@Param("strTime") Date strTime, @Param("endTime") Date endTime);

    Long getBillInfoSum(@Param("param") Map<String, Object> param);
}
