package com.mpool.admin.mapperUtils.bill;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.exception.AdminException;
import com.mpool.admin.exception.ExceptionCode;
import com.mpool.admin.mapperUtils.bill.mapperBase.UserPayBillItemBaseMapper;
import com.mpool.admin.module.bill.mapper.UserPayBillItemMapper;
import com.mpool.admin.module.bill.model.BillItemCSVModel;
import com.mpool.admin.module_zec.bill.mapper.UserPayBillItemZecMapper;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.common.model.account.bill.UserPayBillItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/5/13 20:33
 */

@Component
@Slf4j
public class UserPayBillItemMapperUtils implements UserPayBillItemBaseMapper<UserPayBillItem> {
    @Autowired
    private UserPayBillItemMapper userPayBillItemMapper;
    @Autowired
    private UserPayBillItemZecMapper userPayBillItemZecMapper;

    protected UserPayBillItemBaseMapper getInstance(){
        String currency = AdminSecurityUtils.getCurrencyName();
        if(currency == null || currency.equals("BTC")){
            return userPayBillItemMapper;
        }else if(currency.equals("ZEC")){
            return userPayBillItemZecMapper;
        }
        log.error("UserPayBillItemMapperUtils 调用失败， currency={}", AdminSecurityUtils.getCurrencyName());
        throw new AdminException(ExceptionCode.service_not_exist);
    }

    @Override
    public IPage<Map<String, Object>> getDuePayItems(IPage<Map<String, Object>> iPage, Map<String, Object> mapper, Long day) {
        return getInstance().getDuePayItems(iPage, mapper, day);
    }

    @Override
    public List<UserPayBillItem> getDuePayBtcSum(String username) {
        return getInstance().getDuePayBtcSum(username);
    }

    @Override
    public int updateBillNumber(String billNumber, List<Long> itemIds) {
        return getInstance().updateBillNumber(billNumber, itemIds);
    }

    @Override
    public List<LinkedHashMap<String, Object>> getBillItemsByBillNumber(String billNumber) {
        return getInstance().getBillItemsByBillNumber(billNumber);
    }

    @Override
    public Long getAllBtcByBillNumber(String billNumber) {
        return getInstance().getAllBtcByBillNumber(billNumber);
    }

    @Override
    public List<BillItemCSVModel> getBillItemCSVByBillNumber(String billNumber) {
        return getInstance().getBillItemCSVByBillNumber(billNumber);
    }

    @Override
    public IPage<Map<String, Object>> getPayBillItemList(IPage<Map<String, Object>> page, Map<String, Object> param) {
        return getInstance().getPayBillItemList(page, param);
    }

    @Override
    public List<UserPayBillItem> getUserPayInPayment(List<Long> userIds) {
        return getInstance().getUserPayInPayment(userIds);
    }

    @Override
    public List<UserPayBillItem> getUserPayInPaymentByMasterId(List<Long> userList) {
        return getInstance().getUserPayInPaymentByMasterId(userList);
    }

    @Override
    public IPage<UserPayBillItem> getDuePayItemList(IPage<UserPayBillItem> iPage, String username) {
        return getInstance().getDuePayItemList(iPage, username);
    }

    @Override
    public List<UserPayBillItem> exportDuePayItems(String username) {
        return getInstance().exportDuePayItems(username);
    }

    @Override
    public void addPayBillInfo(UserPayBillItem model) {
        getInstance().addPayBillInfo(model);
    }

    @Override
    public void updateListPayBct(Long payBtc, Long id) {
        getInstance().updateListPayBct(payBtc, id);
    }

    @Override
    public List<UserPayBillItem> getUserPayBillItemList() {
        return getInstance().getUserPayBillItemList();
    }

    @Override
    public int updateListRollBack(String billNumber, List<Long> itemIds) {
        return getInstance().updateListRollBack(billNumber, itemIds);
    }

    @Override
    public int updateAllRollBack(String billNumber) {
        return getInstance().updateAllRollBack(billNumber);
    }

    @Override
    public int insert(UserPayBillItem userPayBillItem) {
        return getInstance().insert(userPayBillItem);
    }

    @Override
    public int deleteById(Serializable serializable) {
        return getInstance().deleteById(serializable);
    }

    @Override
    public int deleteByMap(Map<String, Object> map) {
        return getInstance().deleteByMap(map);
    }

    @Override
    public int delete(Wrapper<UserPayBillItem> wrapper) {
        return getInstance().delete(wrapper);
    }

    @Override
    public int deleteBatchIds(Collection<? extends Serializable> collection) {
        return getInstance().deleteBatchIds(collection);
    }

    @Override
    public int updateById(UserPayBillItem userPayBillItem) {
        return getInstance().updateById(userPayBillItem);
    }

    @Override
    public int update(UserPayBillItem userPayBillItem, Wrapper<UserPayBillItem> wrapper) {
        return getInstance().update(userPayBillItem, wrapper);
    }

    @Override
    public UserPayBillItem selectById(Serializable serializable) {
        return (UserPayBillItem) getInstance().selectById(serializable);
    }

    @Override
    public List<UserPayBillItem> selectBatchIds(Collection<? extends Serializable> collection) {
        return getInstance().selectBatchIds(collection);
    }

    @Override
    public List<UserPayBillItem> selectByMap(Map<String, Object> map) {
        return getInstance().selectByMap(map);
    }

    @Override
    public UserPayBillItem selectOne(Wrapper<UserPayBillItem> wrapper) {
        return (UserPayBillItem) getInstance().selectOne(wrapper);
    }

    @Override
    public Integer selectCount(Wrapper<UserPayBillItem> wrapper) {
        return getInstance().selectCount(wrapper);
    }

    @Override
    public List<UserPayBillItem> selectList(Wrapper<UserPayBillItem> wrapper) {
        return getInstance().selectList(wrapper);
    }

    @Override
    public List<Map<String, Object>> selectMaps(Wrapper<UserPayBillItem> wrapper) {
        return getInstance().selectMaps(wrapper);
    }

    @Override
    public List<Object> selectObjs(Wrapper<UserPayBillItem> wrapper) {
        return getInstance().selectObjs(wrapper);
    }

    @Override
    public IPage<UserPayBillItem> selectPage(IPage<UserPayBillItem> iPage, Wrapper<UserPayBillItem> wrapper) {
        return getInstance().selectPage(iPage, wrapper);
    }

    @Override
    public IPage<Map<String, Object>> selectMapsPage(IPage<UserPayBillItem> iPage, Wrapper<UserPayBillItem> wrapper) {
        return getInstance().selectMapsPage(iPage, wrapper);
    }
}
