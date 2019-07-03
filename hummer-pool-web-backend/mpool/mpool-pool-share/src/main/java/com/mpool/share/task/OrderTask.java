package com.mpool.share.task;

import com.mpool.share.service.ShareService;
import com.mpool.share.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: stephen
 * @Date: 2019/5/23 18:48
 */

@Component
public class OrderTask {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ShareService shareService;

    /**
     * 检测支付过期订单
     */
    //@Scheduled(cron = "*/10 * * * * ?")
    @Scheduled(cron = "0 */20 * * * ?")
    public void expiredOrderTask(){
        orderService.checkExpiredOrder();
    }

    /**
     * 检测订单的支付情况
     */
    //@Scheduled(cron = "*/10 * * * * ?")
    @Scheduled(cron = "0 */15 * * * ?")
    public void checkHadPayOrder(){
        orderService.checkHadPayOrder();
    }

    /**
     * 检测收款地址的余额是否被转出，如果被转出则回收
     */
    //@Scheduled(cron = "*/10 * * * * ?")
    @Scheduled(cron = "0 0 */2 * * ?")
    public void checkWalletAddress(){
        shareService.checkWalletAddress();
    }

    /**
     * 每日收益账单
     */
    //@Scheduled(cron = "*/10 * * * * ?")
    @Scheduled(cron = "0 10 0 * * ?")
    public void taskSettlement() {
        shareService.taskSettlement();
        shareService.taskBalance();
    }

    /**
     * 每日收益系数
     */
    //@Scheduled(cron = "*/10 * * * * ?")
    @Scheduled(cron = "0 1 0 * * ?")
    public void taskShareRate(){
        shareService.taskShareRate();
    }
}
