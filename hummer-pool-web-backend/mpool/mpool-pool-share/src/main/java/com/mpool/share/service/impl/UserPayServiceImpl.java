package com.mpool.share.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpool.share.mapper.UserPayMapper;
import com.mpool.common.model.share.UserPayModel;
import com.mpool.share.service.UserPayService;
import com.mpool.share.utils.AccountSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: stephen
 * @Date: 2019/5/29 20:41
 */

@Service
public class UserPayServiceImpl implements UserPayService {
    @Autowired
    private UserPayMapper userPayMapper;

    @Override
    public String getUserWalletAddress() {
        UserPayModel model = new UserPayModel();
        model.setPuid(AccountSecurityUtils.getUser().getUserId());
        model = userPayMapper.selectOne(new QueryWrapper<>(model));
        if(model == null){
            return null;
        }
        return model.getWalletAddress();
    }

    @Override
    public Double getUserBalance(Long userId) {
        return userPayMapper.getBalanceByUserId(userId);
    }

    @Override
    public Double getRecommendTotal(Long userId) {
        UserPayModel model = new UserPayModel();
        model.setPuid(userId);
        model = userPayMapper.selectOne(new QueryWrapper<>(model));
        return model.getRecommendTotal();
    }
}
