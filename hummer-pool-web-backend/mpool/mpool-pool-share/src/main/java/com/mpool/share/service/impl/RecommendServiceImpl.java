package com.mpool.share.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpool.common.model.billenem.BillTypeEnum;
import com.mpool.common.model.share.UserBill;
import com.mpool.common.model.share.UserInvite;
import com.mpool.common.util.DateUtil;
import com.mpool.share.mapper.recommend.RecommendBillItemMapper;
import com.mpool.share.mapper.recommend.UserInviteMapper;
import com.mpool.share.mapper.recommend.UserRecommendMapper;
import com.mpool.common.model.share.UserRecommend;
import com.mpool.share.model.RecommendBillItem;
import com.mpool.share.service.RecommendService;
import com.mpool.share.service.ShareService;
import com.mpool.share.service.UserBillService;
import com.mpool.share.service.UserPayService;
import com.mpool.share.utils.KeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: stephen
 * @Date: 2019/6/4 16:39
 */

@Service
public class RecommendServiceImpl implements RecommendService {
    @Autowired
    private UserInviteMapper inviteMapper;
    @Autowired
    private UserPayService userPayService;
    @Autowired
    private UserRecommendMapper recommendMapper;
    @Autowired
    private RecommendBillItemMapper billItemMapper;
    @Autowired
    private UserBillService userBillService;
    @Autowired
    private ShareService shareService;

    @Override
    public Map<String, Object> getNormalInfo(Long userId) {
        //获得已邀请人数量
        Integer invitedCount = inviteMapper.getInviteCountByUid(userId);

        //获得已邀请人正在挖矿人数
        Integer workingCount = inviteMapper.getWorkingInviteCountByUid(userId);

        Double recommendTotal = userPayService.getRecommendTotal(userId);

        UserRecommend userRecommend = new UserRecommend();
        userRecommend.setPuid(userId);
        userRecommend = recommendMapper.selectOne(new QueryWrapper<>(userRecommend));

        Map<String, Object> map = new HashMap<>();
        map.put("invitedCount", invitedCount);
        map.put("recommendTotal", recommendTotal);
        map.put("workingCount", workingCount);
        map.put("rate", userRecommend.getRate());
        map.put("advp", 100);
        map.put("advr", 5);

        return map;
    }

    @Override
    public List<Map<String, Object>> getInviteUserList(Long userId) {
        return inviteMapper.getInviteUserInfoByUid(userId);
    }

    @Override
    public List<Map<String, Object>> getInviteShareList(Long userId) {
        return inviteMapper.getInviteShareByUid(userId);
    }

    @Override
    public void setRecommendShare(Map<Long, Double> shareMap) {
        if(!shareMap.isEmpty()){
            Date date = new Date();
            String ymd = DateUtil.getYYYYMMdd(date);
            List<UserBill> userBillList = new ArrayList<>();
            Set<Long> uids = shareMap.keySet();
            List<UserInvite> inviteList = inviteMapper.getRecommendUserByInviteIds(uids);
            if(!inviteList.isEmpty()) {
                //返佣账单额外信息
                List<RecommendBillItem> recommendBillItems = new ArrayList<>();
                for (UserInvite invite : inviteList) {
                    Long inviteUid = invite.getInviteUid();
                    if(shareMap.containsKey(inviteUid)){
                        Double reEarn = shareMap.get(inviteUid) * invite.getRate() / 100.0D;

                        //返佣账单
                        UserBill userBill = new UserBill();
                        userBill.setId(KeyUtil.genUniqueKey());
                        userBill.setPuid(invite.getRecommendUid());
                        userBill.setDay(ymd);
                        userBill.setEarn(reEarn);
                        userBill.setBillType(BillTypeEnum.BILL_TYPE_BROKERAGE.getCode());
                        userBill.setDiscrible("返佣");
                        userBill.setCreateTime(date);
                        userBillList.add(userBill);

                        //账单额外信息
                        RecommendBillItem recommendBillItem = new RecommendBillItem();
                        recommendBillItem.setBillId(userBill.getId());
                        recommendBillItem.setInviteUid(invite.getInviteUid());
                        recommendBillItem.setRecommendUid(invite.getRecommendUid());
                        recommendBillItem.setCreateTime(date);
                        recommendBillItems.add(recommendBillItem);
                    }
                }
                billItemMapper.inserts(recommendBillItems);
            }

            if(!userBillList.isEmpty()){
                userBillService.insertBills(userBillList);
                shareService.taskBalance();
            }
        }
    }
}
