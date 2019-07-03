package com.mpool.admin.mapperUtils.pool;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.exception.AdminException;
import com.mpool.admin.exception.ExceptionCode;
import com.mpool.admin.mapperUtils.pool.mapperBase.MiningWorkersBaseMapper;
import com.mpool.admin.module.pool.mapper.MiningWorkersMapper;
import com.mpool.admin.module_zec.pool.mapper.MiningWorkersZecMapper;
import com.mpool.admin.module_zec.pool.model.MiningWorkersZec;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.common.model.pool.MiningWorkers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/5/14 15:22
 */
@Component
@Slf4j
public class MiningWorkersMapperUtils implements MiningWorkersBaseMapper<MiningWorkers> {
    @Autowired
    private MiningWorkersMapper miningWorkersMapper;
    @Autowired
    private MiningWorkersZecMapper miningWorkersZecMapper;

    protected MiningWorkersBaseMapper getInstance(){
        String currency = AdminSecurityUtils.getCurrencyName();
        return getInstance(currency);
    }

    protected MiningWorkersBaseMapper getInstance(String currencyName){
        if(currencyName == null || currencyName.equals("BTC")){
            return miningWorkersMapper;
        }else if(currencyName.equals("ZEC")){
            return miningWorkersZecMapper;
        }
        log.error("MiningWorkersMapperUtils 调用失败， currency={}", AdminSecurityUtils.getCurrencyName());
        throw new AdminException(ExceptionCode.service_not_exist);
    }

    @Override
    public MiningWorkers getUserMiningWorkers(Long userId) {
        return getInstance().getUserMiningWorkers(userId);
    }

    @Override
    public List<MiningWorkers> getUserMiningWorkersList(List<Long> userId) {
        return getInstance().getUserMiningWorkersList(userId);
    }

    @Override
    public MiningWorkers getMiningWorkersByPool() {
        return getInstance().getMiningWorkersByPool();
    }

    @Override
    public Integer getPoolWorkerActiveCount() {
        return getInstance().getPoolWorkerActiveCount();
    }

    public Integer getPoolWorkerActiveCountByCurrency(String currencyName) {
        return getInstance(currencyName).getPoolWorkerActiveCount();
    }

    @Override
    public int insert(MiningWorkers miningWorkersZec) {
        return getInstance().insert(miningWorkersZec);
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
    public int delete(Wrapper<MiningWorkers> wrapper) {
        return getInstance().delete(wrapper);
    }

    @Override
    public int deleteBatchIds(Collection<? extends Serializable> collection) {
        return getInstance().deleteBatchIds(collection);
    }

    @Override
    public int updateById(MiningWorkers miningWorkersZec) {
        return getInstance().updateById(miningWorkersZec);
    }

    @Override
    public int update(MiningWorkers miningWorkersZec, Wrapper<MiningWorkers> wrapper) {
        return getInstance().update(miningWorkersZec, wrapper);
    }

    @Override
    public MiningWorkersZec selectById(Serializable serializable) {
        return (MiningWorkersZec) getInstance().selectById(serializable);
    }

    @Override
    public List<MiningWorkers> selectBatchIds(Collection<? extends Serializable> collection) {
        return getInstance().selectBatchIds(collection);
    }

    @Override
    public List<MiningWorkers> selectByMap(Map<String, Object> map) {
        return getInstance().selectByMap(map);
    }

    @Override
    public MiningWorkers selectOne(Wrapper<MiningWorkers> wrapper) {
        return (MiningWorkers) getInstance().selectOne(wrapper);
    }

    @Override
    public Integer selectCount(Wrapper<MiningWorkers> wrapper) {
        return getInstance().selectCount(wrapper);
    }

    @Override
    public List<MiningWorkers> selectList(Wrapper<MiningWorkers> wrapper) {
        return getInstance().selectList(wrapper);
    }

    @Override
    public List<Map<String, Object>> selectMaps(Wrapper<MiningWorkers> wrapper) {
        return getInstance().selectMaps(wrapper);
    }

    @Override
    public List<Object> selectObjs(Wrapper<MiningWorkers> wrapper) {
        return getInstance().selectObjs(wrapper);
    }

    @Override
    public IPage<MiningWorkers> selectPage(IPage<MiningWorkers> iPage, Wrapper<MiningWorkers> wrapper) {
        return getInstance().selectPage(iPage, wrapper);
    }

    @Override
    public IPage<Map<String, Object>> selectMapsPage(IPage<MiningWorkers> iPage, Wrapper<MiningWorkers> wrapper) {
        return getInstance().selectMapsPage(iPage, wrapper);
    }
}
