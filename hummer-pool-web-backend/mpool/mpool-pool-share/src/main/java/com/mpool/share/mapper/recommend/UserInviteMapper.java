package com.mpool.share.mapper.recommend;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.share.UserInvite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: stephen
 * @Date: 2019/6/4 11:55
 */

@Mapper
public interface UserInviteMapper extends BaseMapper<UserInvite> {

    /**
     * 获得用户推荐人列表
     * @param rIds
     * @return
     */
    List<UserInvite> getRecommendUserByInviteIds(@Param("rIds") Set<Long> rIds);

    /**
     * 获得用户邀请过的其他用户数量
     * @param userId
     * @return
     */
    Integer getInviteCountByUid(@Param("userId") Long userId);

    /**
     * 获得用户邀请过的其他用户信息
     * @param userId
     * @return
     */
    List<Map<String, Object>> getInviteUserInfoByUid(@Param("userId") Long userId);

    /**
     * 获得用户邀请过的其他用户返佣收益情况
     * @param userId
     * @return
     */
    List<Map<String, Object>> getInviteShareByUid(@Param("userId") Long userId);

    /**
     * 获得用户邀请过的正在挖矿的其他用户数量
     * @param userId
     * @return
     */
    Integer getWorkingInviteCountByUid(@Param("userId") Long userId);

}
