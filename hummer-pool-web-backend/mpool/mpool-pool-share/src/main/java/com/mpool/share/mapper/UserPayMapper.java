package com.mpool.share.mapper;

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
    List<UserPayModel> getUserByUserIds(@Param("ids") Set<Long> ids);

    void updateUserPayBatch(@Param("userPayModelList") List<UserPayModel> userPayModelList);

    void updateWalletAddress(@Param("puid") Long puid, @Param("address") String address);

    /**
     * 获取用户余额
     * @param puid
     * @return
     */
    Double getBalanceByUserId(@Param("puid") Long puid);
}
