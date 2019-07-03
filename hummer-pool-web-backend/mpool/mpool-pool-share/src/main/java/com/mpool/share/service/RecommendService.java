package com.mpool.share.service;

import java.util.List;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/6/4 16:38
 */
public interface RecommendService {
    /**
     * 获得邀请基本信息
     * @param userId
     * @return
     */
    Map<String, Object> getNormalInfo(Long userId);

    /**
     * 获得邀请人列表
     * @param userId
     * @return
     */
    List<Map<String, Object>> getInviteUserList(Long userId);

    /**
     * 获得邀请人返佣信息
     * @param userId
     * @return
     */
    List<Map<String, Object>> getInviteShareList(Long userId);

    /**
     * 处理返佣收益
     * @param shareMap
     */
    void setRecommendShare(Map<Long, Double> shareMap);
}
