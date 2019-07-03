package com.mpool.share.task;

import com.mpool.share.mapper.UserNotifyMapper;
import com.mpool.share.model.NotifyResultModel;
import com.mpool.share.model.OrderNotifyModel;
import com.mpool.share.model.UserNotify;
import com.mpool.share.service.EmailService;
import com.mpool.share.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 到期提醒任务
 * @Author: stephen
 * @Date: 2019/6/5 15:12
 */

@Component
public class OrderNotifyTask implements Runnable {
    @Autowired
    private UserNotifyMapper userNotifyMapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    private List<OrderNotifyModel> notifyModelList;

    public void setNotifyModelList(List<OrderNotifyModel> notifyModelList) {
        this.notifyModelList = notifyModelList;
    }


    @Override
    public void run() {
        if(!notifyModelList.isEmpty()) {
            List<Long> ids = notifyModelList.stream().map(model -> model.getUserId()).collect(Collectors.toList());
            List<NotifyResultModel> userNotifyList = userNotifyMapper.getNotifyByUserIds(ids);
            if(!userNotifyList.isEmpty()) {
                for (OrderNotifyModel model : this.notifyModelList) {
                    Long uid = model.getUserId();
                    for(NotifyResultModel notify : userNotifyList){
                        if(uid.equals(notify.getPuid())){
                            String content = null;
                            if(model.getOffsetDay().equals(7)){
                                if(notify.isBefore7Notify()){
                                    //订单到期前7天提醒
                                    content = "将于近日";
                                }
                            }else if(model.getOffsetDay().equals(1)){
                                if(notify.isBefore1Notify()){
                                    //订单到期前1天提醒
                                    content = "将于近日";
                                }
                            }else if(model.getOffsetDay().equals(-1)){
                                if(notify.isExpiredNotify()){
                                    //订单失效后提醒
                                    content = "已";
                                }
                            }

                            if(notify.isPhoneNotify() && notify.getPhone() != null){
                                String templateParam = String.format("{\"msg\":\"%s\"}", content);
                                smsService.sendSms(notify.getPhone(), "SMS_167195930", templateParam, notify.getPhone());
                            }
                            if(notify.isEmailNotify() && notify.getEmail() != null){
                                Map<String, Object> param = new HashMap<>();
                                param.put("msg", content);
                                IContext contex = new Context(Locale.getDefault(), param);
                                emailService.sendAlarmEmail(notify.getEmail(), "email/notifyOrder.html", "FlyHash到期提醒", contex);
                            }

                            break;
                        }
                    }
                }
            }
        }
    }
}
