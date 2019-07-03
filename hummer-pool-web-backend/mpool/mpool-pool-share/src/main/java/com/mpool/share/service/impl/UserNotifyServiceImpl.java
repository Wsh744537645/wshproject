package com.mpool.share.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpool.share.mapper.UserNotifyMapper;
import com.mpool.share.model.UserNotify;
import com.mpool.share.service.UserNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: stephen
 * @Date: 2019/5/31 9:57
 */

@Service
public class UserNotifyServiceImpl implements UserNotifyService {
    @Autowired
    private UserNotifyMapper notifyMapper;

    @Override
    public UserNotify findOne(Long userId) {
        UserNotify notify = new UserNotify();
        notify.setPuid(userId);
        notify = notifyMapper.selectOne(new QueryWrapper<>(notify));
        return notify;
    }

    @Override
    @Transactional
    public void updateUserNotify(UserNotify notify) {
        UserNotify queryUserNotify = new UserNotify();
        queryUserNotify.setPuid(notify.getPuid());
        notifyMapper.update(notify, new QueryWrapper<>(queryUserNotify));
    }
}
