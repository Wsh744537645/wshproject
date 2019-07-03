package com.mpool.share.admin.module.log.service.impl;

import com.mpool.share.admin.module.log.mapper.LogAdminOperationMpper;
import com.mpool.share.admin.module.log.service.LogAdminOperationService;
import com.mpool.share.admin.utils.AdminSecurityUtils;
import com.mpool.common.model.log.LogUserOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class LogAdminOperationServiceImpl implements LogAdminOperationService {
    @Autowired
    private LogAdminOperationMpper userOperationMapper;

    @Override
    public void insert(LogUserOperation entity) {
        entity.setCurrencyName(AdminSecurityUtils.getCurrencyName());
        userOperationMapper.insert(entity);
    }

    @Override
    public void inserts(List<LogUserOperation> entitys) {

    }

    @Override
    public void update(LogUserOperation entity) {
        entity.setCurrencyName(AdminSecurityUtils.getCurrencyName());
        userOperationMapper.updateById(entity);
    }

    @Override
    public void delete(Serializable id) {
        userOperationMapper.deleteById(id);
    }

    @Override
    public LogUserOperation findById(Serializable id) {
        return userOperationMapper.selectById(id);
    }
}
