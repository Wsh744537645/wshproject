package com.mpool.share.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.share.mapper.UserBillMapper;
import com.mpool.common.model.share.UserBill;
import com.mpool.share.service.UserBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/5/28 14:56
 */

@Service
public class UserBillServiceImpl implements UserBillService {
    @Autowired
    private UserBillMapper userBillMapper;

    @Override
    public void insertBills(List<UserBill> userBills) {
        userBillMapper.insertBills(userBills);
    }

    @Override
    public List<UserBill> getAllActivityBills() {
        return userBillMapper.getAllActivityBills();
    }

    @Override
    public void setBillEnabledState(List<UserBill> userBills) {
        userBillMapper.setBillEnabledState(userBills);
    }

    @Override
    public void updateBillStateAndBalance(List<UserBill> userBills) {
        userBillMapper.updateBillStateAndBalance(userBills);
    }

    @Override
    public IPage<Map<String, Object>> getDailyAcceptOrderList(IPage<Map<String, Object>> page, Long userId, Long begTime, Long endTime) {
        IPage<Map<String, Object>> mapList = userBillMapper.getDailyAcceptOrderList(page, userId, begTime, endTime);
        List<Map<String, Object>> banlanceMap = userBillMapper.getDailyBalance(userId, begTime, endTime);

        List<Map<String, Object>> recodes = mapList.getRecords();
        for(Map<String, Object> data : recodes){
            Long day = Long.parseLong(data.get("day").toString());
            for(Map<String, Object> tmp : banlanceMap){
                Long day1 = Long.parseLong(tmp.get("day").toString());
                if(day.equals(day1)){
                    data.putAll(tmp);
                    break;
                }
            }
        }
        return mapList;
    }

    @Override
    public IPage<Map<String, Object>> getBillListByOrderid(IPage<Map<String, Object>> page, Integer state, Long userId, String orderId) {
        return userBillMapper.getBillListByOrderid(page, state, userId, orderId);
    }
}
