package com.mpool.share.admin.module.bill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.share.UserPayModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @Author: stephen
 * @Date: 2019/5/29 17:49
 */

@Mapper
public interface UserPayMapper extends BaseMapper<UserPayModel> {
    /**
     * 获取用户余额
     * @param puid
     * @return
     */
    Double getBalanceByUserId(@Param("puid") Long puid);

    void updateUserBalance(@Param("userId") Long userId, @Param("balance") Double balance);

    List<UserPayModel> getUsersPay(@Param("userIds") Set<Long> userIds);

    void updateUserBalanceBatch(@Param("users") List<UserPayModel> users);
}
