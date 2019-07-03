package com.mpool.share.admin.module.bill.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.util.EncryUtil;
import com.mpool.share.admin.exception.AdminException;
import com.mpool.share.admin.exception.ExceptionCode;
import com.mpool.share.admin.model.User;
import com.mpool.share.admin.module.bill.mapper.UserMapper;
import com.mpool.share.admin.module.bill.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/4/30 11:56
 */

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    /**
     * 用户表操作
     */
    @Autowired
    private UserMapper usersMapper;


    @Override
    public User findByUsername(String username) {
        return usersMapper.findByUsername(username);
    }

    @Override
    public User findByUserId(Long userId) {
        return usersMapper.findById(userId);
    }

    @Override
    public IPage<Map<String, Object>> getUsersInfoList(IPage<Map<String, Object>> page, String username) {
        return usersMapper.getUsersInfoList(page, username);
    }

    @Override
    public void resetPassword(Long userId, String password) {
        User user = usersMapper.findById(userId);
        if(user == null){
            throw new AdminException(ExceptionCode.user_not_exists);
        }

        User userr = new User();
        userr.setPassword(EncryUtil.encrypt(password));
        userr.setUserId(user.getUserId());
        usersMapper.updateById(userr);
    }
}
