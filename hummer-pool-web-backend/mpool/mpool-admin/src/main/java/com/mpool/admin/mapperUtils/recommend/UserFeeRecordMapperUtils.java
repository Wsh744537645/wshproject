package com.mpool.admin.mapperUtils.recommend;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.exception.AdminException;
import com.mpool.admin.exception.ExceptionCode;
import com.mpool.admin.mapperUtils.recommend.mapperBase.UserFeeRecordBaseMapper;
import com.mpool.admin.module.recommend.mapper.UserFeeRecordMapper;
import com.mpool.admin.module_zec.recommend.mapper.UserFeeRecordZecMapper;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.common.model.account.UserFeeRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/5/14 16:41
 */

@Component
@Slf4j
public class UserFeeRecordMapperUtils implements UserFeeRecordBaseMapper<UserFeeRecord> {
    @Autowired
    private UserFeeRecordMapper userFeeRecordMapper;
    @Autowired
    private UserFeeRecordZecMapper userFeeRecordZecMapper;

    protected UserFeeRecordBaseMapper getInstance(){
        String currency = AdminSecurityUtils.getCurrencyName();
        if(currency == null || currency.equals("BTC")){
            return userFeeRecordMapper;
        }else if(currency.equals("ZEC")){
            return userFeeRecordZecMapper;
        }
        log.error("UserFeeRecordMapperUtils 调用失败， currency={}", AdminSecurityUtils.getCurrencyName());
        throw new AdminException(ExceptionCode.service_not_exist);
    }

    @Override
    public void insets(List<UserFeeRecord> list) {
        getInstance().insets(list);
    }

    @Override
    public int insert(UserFeeRecord userFeeRecord) {
        return getInstance().insert(userFeeRecord);
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
    public int delete(Wrapper<UserFeeRecord> wrapper) {
        return getInstance().delete(wrapper);
    }

    @Override
    public int deleteBatchIds(Collection<? extends Serializable> collection) {
        return getInstance().deleteBatchIds(collection);
    }

    @Override
    public int updateById(UserFeeRecord userFeeRecord) {
        return getInstance().updateById(userFeeRecord);
    }

    @Override
    public int update(UserFeeRecord userFeeRecord, Wrapper<UserFeeRecord> wrapper) {
        return getInstance().update(userFeeRecord, wrapper);
    }

    @Override
    public UserFeeRecord selectById(Serializable serializable) {
        return (UserFeeRecord) getInstance().selectById(serializable);
    }

    @Override
    public List<UserFeeRecord> selectBatchIds(Collection<? extends Serializable> collection) {
        return getInstance().selectBatchIds(collection);
    }

    @Override
    public List<UserFeeRecord> selectByMap(Map<String, Object> map) {
        return getInstance().selectByMap(map);
    }

    @Override
    public UserFeeRecord selectOne(Wrapper<UserFeeRecord> wrapper) {
        return (UserFeeRecord) getInstance().selectOne(wrapper);
    }

    @Override
    public Integer selectCount(Wrapper<UserFeeRecord> wrapper) {
        return getInstance().selectCount(wrapper);
    }

    @Override
    public List<UserFeeRecord> selectList(Wrapper<UserFeeRecord> wrapper) {
        return getInstance().selectList(wrapper);
    }

    @Override
    public List<Map<String, Object>> selectMaps(Wrapper<UserFeeRecord> wrapper) {
        return getInstance().selectMaps(wrapper);
    }

    @Override
    public List<Object> selectObjs(Wrapper<UserFeeRecord> wrapper) {
        return getInstance().selectObjs(wrapper);
    }

    @Override
    public IPage<UserFeeRecord> selectPage(IPage<UserFeeRecord> iPage, Wrapper<UserFeeRecord> wrapper) {
        return getInstance().selectPage(iPage, wrapper);
    }

    @Override
    public IPage<Map<String, Object>> selectMapsPage(IPage<UserFeeRecord> iPage, Wrapper<UserFeeRecord> wrapper) {
        return getInstance().selectMapsPage(iPage, wrapper);
    }
}
