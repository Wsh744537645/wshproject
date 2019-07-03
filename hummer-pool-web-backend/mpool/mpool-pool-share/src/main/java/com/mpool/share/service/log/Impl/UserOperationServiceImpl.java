package com.mpool.share.service.log.Impl;

import com.mpool.share.mapper.log.UserOperationMapper;
import com.mpool.share.service.log.UserOperationService;
import com.mpool.common.model.log.LogUserOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * wgg
 * 用户端用户端日志操作收集
 */
@Service
public class UserOperationServiceImpl implements UserOperationService {

    @Autowired
    private UserOperationMapper userOperationMapper;

    @Override
    public void insert(LogUserOperation entity) {
        userOperationMapper.insert(entity);
    }

    @Override
    public void inserts(List<LogUserOperation> entitys) {

    }

    @Override
    public void update(LogUserOperation entity) {
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
