package com.mpool.share.admin.module.bill.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.share.admin.model.User;

import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/4/30 11:33
 */
public interface UserService {

    User findByUsername(String username);

    User findByUserId(Long userId);

    IPage<Map<String, Object>> getUsersInfoList(IPage<Map<String, Object>> page, String username);

    void resetPassword(Long userId, String password);
}
