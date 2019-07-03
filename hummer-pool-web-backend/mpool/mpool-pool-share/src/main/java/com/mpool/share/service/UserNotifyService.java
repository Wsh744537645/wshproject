package com.mpool.share.service;

import com.mpool.share.model.UserNotify;

/**
 * @Author: stephen
 * @Date: 2019/5/31 9:56
 */
public interface UserNotifyService {
    UserNotify findOne(Long userId);

    void updateUserNotify(UserNotify notify);
}
