package com.mpool.admin.mapperUtils.bill.mapperBase;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.module.bill.model.BillItemCSVModel;
import com.mpool.common.model.account.bill.UserPayBillItem;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/5/13 20:32
 */
public interface UserPayBillItemBaseMapper<T> extends BaseMapper<T> {
    /**
     * 获取待生成账单列表
     *
     * @param iPage
     * @param
     * @return
     */
    IPage<Map<String, Object>> getDuePayItems(IPage<Map<String, Object>> iPage,
                                              @Param("mapper") Map<String, Object> mapper, @Param("day") Long day);
    //余额合计
    List<UserPayBillItem> getDuePayBtcSum(@Param("username") String username);

    /**
     * 更新Billnumber 字段
     *
     * @param billNumber
     * @param itemIds
     * @return
     */
    int updateBillNumber(@Param("billNumber") String billNumber, @Param("itemIds") List<Long> itemIds);

    /**
     * 查询
     *
     * @param billNumber
     * @return
     */
    List<LinkedHashMap<String, Object>> getBillItemsByBillNumber(@Param("billNumber") String billNumber);

    /**
     * 获得一个支付单下面的所有btc
     *
     * @param billNumber
     * @return
     */
    Long getAllBtcByBillNumber(@Param("billNumber") String billNumber);

    /**
     * 获得csv数据
     *
     * @param billNumber
     * @return
     */
    List<BillItemCSVModel> getBillItemCSVByBillNumber(@Param("billNumber") String billNumber);

    /**
     * 获得未支付的账单
     *
     * @param page
     * @param param
     * @return
     */
    IPage<Map<String, Object>> getPayBillItemList(IPage<Map<String, Object>> page, Map<String, Object> param);

    /**
     * 获得用户支付中的 btc
     *
     * @param userIds
     * @return
     */
    List<UserPayBillItem> getUserPayInPayment(List<Long> userIds);

    List<UserPayBillItem> getUserPayInPaymentByMasterId(@Param("userList") List<Long> userList);

    /**
     * wgg 获取待生成账单列表
     *
     * @param iPage
     * @param username
     * @return
     */
    IPage<UserPayBillItem> getDuePayItemList(IPage<UserPayBillItem> iPage, @Param("username") String username);

    /**
     * wgg 导出待生成账单列表
     *
     * @param username
     * @return
     */
    List<UserPayBillItem> exportDuePayItems(@Param("username") String username);

    void addPayBillInfo(UserPayBillItem model);

    /**
     * 批量修改待生成订单余额
     * @param payBtc
     * @param id
     */
    void updateListPayBct(@Param("payBtc") Long payBtc,@Param("id") Long id);
    List<UserPayBillItem> getUserPayBillItemList();

    /**
     * 批量回滚账单，撤销绑定的billNumber
     * @param billNumber
     * @param itemIds
     */
    int updateListRollBack(@Param("billNumber") String billNumber, @Param("itemIds") List<Long> itemIds);

    /**
     * 批量回滚账单，撤销所有绑定的billNumber
     * @param billNumber
     */
    int updateAllRollBack(@Param("billNumber") String billNumber);
}
