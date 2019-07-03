package com.mpool.share.service.order.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.product.OrderPowerModel;
import com.mpool.share.exception.ExceptionCode;
import com.mpool.share.exception.ShareException;
import com.mpool.share.mapper.UserBillMapper;
import com.mpool.share.mapper.order.OrderAcceptMapper;
import com.mpool.share.mapper.order.OrderPowerMapper;
import com.mpool.common.model.share.UserBill;
import com.mpool.share.resultVo.OrderListVO;
import com.mpool.share.service.order.OrderAcceptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: stephen
 * @Date: 2019/5/29 16:19
 */

@Service
@Slf4j
public class OrderAcceptServiceImpl implements OrderAcceptService {
    @Autowired
    private OrderAcceptMapper orderAcceptMapper;
    @Autowired
    private UserBillMapper userBillMapper;
    @Autowired
    private OrderPowerMapper orderPowerMapper;

    @Override
    public IPage<OrderListVO> getAcceptOrderList(Long userId, IPage<OrderListVO> page, Integer state) {
        return orderAcceptMapper.getAcceptOrderListByState(page, userId, state);
    }

    @Override
    public Map<String, Object> getAcceptNormalByOrderId(Long userId, String acceptId) {
        Map<String, Object> map = orderAcceptMapper.getNormalInfo(userId, acceptId);
        if(map != null) {
            if (!map.containsKey("workTime")) {
                map.put("workTime", new Date());
            }
            if (!map.containsKey("expireTime")) {
                map.put("expireTime", new Date());
            }
        }
        return map;
    }

    @Override
    public Map<String, Object> getOrderPayInfo(Long userId, String acceptId) {
        return orderAcceptMapper.getOrderPayInfo(userId, acceptId);
    }

    @Override
    public Map<String, Object> getOrderShareInfo(Long userId, String acceptId) {
        Map<String, Object> map = orderAcceptMapper.getNormalInfo(userId, acceptId);
        if(map == null){
            log.error("【获取算力订单信息失败】acceptId={}", acceptId);
            throw new ShareException(ExceptionCode.order_id_not_exists);
        }
        Map<String, Object> result = new HashMap<>();
        Integer state = Integer.parseInt(map.get("state").toString());
        Integer period = Integer.parseInt(map.get("period").toString());
        Double accept = Double.parseDouble(map.get("accept").toString());
        Double totalShare = 0D;
        Integer acceptDay = 0;
        Integer powerDay = 0;
        if(state.equals(0) || state.equals(3)){
            //有账单
            String orderId = map.get("id").toString();
            List<UserBill> userBillList = userBillMapper.getBillByOrderId(orderId);
            if(userBillList.isEmpty()){
                if(state.equals(0)){
                    //订单正在产生收益,计算电费天数
                    List<String> ids = new ArrayList<>();
                    ids.add(orderId);
                    List<OrderPowerModel> orderPowerModelList = orderPowerMapper.getAcceptOrderPowerDay(ids);
                    Map<String, Integer> powerDayMap = new HashMap<>();
                    for (OrderPowerModel model : orderPowerModelList) {
                        if (!powerDayMap.containsKey(model.getAcceptId())) {
                            powerDayMap.put(model.getAcceptId(), model.getDuration());
                        } else {
                            powerDayMap.put(model.getAcceptId(), powerDayMap.get(model.getAcceptId()) + model.getDuration());
                        }
                    }
                    powerDay = powerDayMap.get(orderId);
                }
            }else{
                for(UserBill userBill : userBillList){
                    totalShare += userBill.getEarn();
                }
                acceptDay = userBillList.get(0).getAcceptDay();
                powerDay = userBillList.get(0).getPowerDay();
            }
        }

        result.put("orderId", acceptId);
        result.put("state", state);
        result.put("period", period);
        result.put("totalShare", totalShare);
        result.put("accept", accept);
        result.put("acceptDay", acceptDay);
        result.put("powerDay", powerDay);

        return result;
    }

    @Override
    public Map<String, Object> getCurAcceptTotal(Long userId) {
        return orderAcceptMapper.getCurAcceptTotal(userId);
    }
}
