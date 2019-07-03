package com.mpool.admin.mapperUtils.bill;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.exception.AdminException;
import com.mpool.admin.exception.ExceptionCode;
import com.mpool.admin.mapperUtils.bill.mapperBase.UserPayBillBaseMapper;
import com.mpool.admin.module.bill.mapper.UserPayBillMapper;
import com.mpool.admin.module_zec.bill.mapper.UserPayBillZecMapper;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.common.model.account.bill.UserPayBill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/5/13 20:42
 */

@Component
@Slf4j
public class UserPayBillMapperUtils implements UserPayBillBaseMapper<UserPayBill> {
    @Autowired
    private UserPayBillMapper userPayBillMapper;
    @Autowired
    private UserPayBillZecMapper userPayBillZecMapper;

    protected UserPayBillBaseMapper getInstance(){
        String currency = AdminSecurityUtils.getCurrencyName();
        if(currency == null || currency.equals("BTC")){
            return userPayBillMapper;
        }else if(currency.equals("ZEC")){
            return userPayBillZecMapper;
        }
        log.error("UserPayBillMapperUtils 调用失败， currency={}", AdminSecurityUtils.getCurrencyName());
        throw new AdminException(ExceptionCode.service_not_exist);
    }

    @Override
    public IPage<Map<String, Object>> getBillInfo(IPage<Map<String, Object>> iPage, Map<String, Object> param) {
        return getInstance().getBillInfo(iPage, param);
    }

    @Override
    public UserPayBill getBillInfoByNumOrTxid(String billNum, String txid) {
        return getInstance().getBillInfoByNumOrTxid(billNum, txid);
    }

    @Override
    public IPage<Map<String, Object>> getBillInfoByDate(IPage<Map<String, Object>> iPage, Date strTime, Date endTime) {
        return getInstance().getBillInfoByDate(iPage, strTime, endTime);
    }

    @Override
    public List<UserPayBill> exportBillList(Date strTime, Date endTime) {
        return getInstance().exportBillList(strTime, endTime);
    }

    @Override
    public Long getBillInfoSum(Map<String, Object> param) {
        return getInstance().getBillInfoSum(param);
    }

    @Override
    public int insert(UserPayBill userPayBill) {
        return getInstance().insert(userPayBill);
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
    public int delete(Wrapper<UserPayBill> wrapper) {
        return getInstance().delete(wrapper);
    }

    @Override
    public int deleteBatchIds(Collection<? extends Serializable> collection) {
        return getInstance().deleteBatchIds(collection);
    }

    @Override
    public int updateById(UserPayBill userPayBill) {
        return getInstance().updateById(userPayBill);
    }

    @Override
    public int update(UserPayBill userPayBill, Wrapper<UserPayBill> wrapper) {
        return getInstance().update(userPayBill, wrapper);
    }

    @Override
    public UserPayBill selectById(Serializable serializable) {
        return (UserPayBill) getInstance().selectById(serializable);
    }

    @Override
    public List<UserPayBill> selectBatchIds(Collection<? extends Serializable> collection) {
        return getInstance().selectBatchIds(collection);
    }

    @Override
    public List<UserPayBill> selectByMap(Map<String, Object> map) {
        return getInstance().selectByMap(map);
    }

    @Override
    public UserPayBill selectOne(Wrapper<UserPayBill> wrapper) {
        return (UserPayBill)getInstance().selectOne(wrapper);
    }

    @Override
    public Integer selectCount(Wrapper<UserPayBill> wrapper) {
        return getInstance().selectCount(wrapper);
    }

    @Override
    public List<UserPayBill> selectList(Wrapper<UserPayBill> wrapper) {
        return getInstance().selectList(wrapper);
    }

    @Override
    public List<Map<String, Object>> selectMaps(Wrapper<UserPayBill> wrapper) {
        return getInstance().selectMaps(wrapper);
    }

    @Override
    public List<Object> selectObjs(Wrapper<UserPayBill> wrapper) {
        return getInstance().selectObjs(wrapper);
    }

    @Override
    public IPage<UserPayBill> selectPage(IPage<UserPayBill> iPage, Wrapper<UserPayBill> wrapper) {
        return getInstance().selectPage(iPage, wrapper);
    }

    @Override
    public IPage<Map<String, Object>> selectMapsPage(IPage<UserPayBill> iPage, Wrapper<UserPayBill> wrapper) {
        return getInstance().selectMapsPage(iPage, wrapper);
    }
}
