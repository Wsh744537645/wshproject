package com.mpool.admin.module.recommend.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.InModel.OutUserFppsRatio;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserFppsRateMapper {
    /**
     * 子账号设置清单 费率 会员类型
     * @param page
     * @param userId
     * @return
     */
    IPage<OutUserFppsRatio> getFppsRateAndUserTypeListMaster(IPage<OutUserFppsRatio> page, @Param("userId") Long userId, @Param("currencyId") Integer currencyId);

    /**
     * 子账号设置清单 费率 会员类型
     * @param page
     * @return
     */
    IPage<OutUserFppsRatio> getAllMemberUserList(IPage<OutUserFppsRatio> page, @Param("currencyId") Integer currencyId);

    IPage<OutUserFppsRatio> getAllMemberUserListGroup(IPage<OutUserFppsRatio> page,@Param("userGroup")Integer userGroup, @Param("currencyId") Integer currencyId);

    IPage<OutUserFppsRatio> getAllMemberUserLists(IPage<OutUserFppsRatio> page, @Param("userId") Long userId,@Param("userGroup")Integer userGroup, @Param("currencyId") Integer currencyId);

}
