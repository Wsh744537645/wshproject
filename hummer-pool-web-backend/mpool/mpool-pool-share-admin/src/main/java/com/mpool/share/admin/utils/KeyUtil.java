package com.mpool.share.admin.utils;

import java.util.Random;

/**
 * @Author: stephen
 * @Date: 2019/5/23 15:19
 */
public class KeyUtil {
    /**
     * 生成总订单id
     * 格式: 时间+随机数
     * @return
     */
    public static synchronized String genUniqueKey() {
        Random random = new Random();
        Integer number = random.nextInt(900000) + 100000;

        return System.currentTimeMillis() + String.valueOf(number);
    }

    /**
     * 生成算力订单id
     * 格式: 时间+随机数
     * @return
     */
    public static synchronized String genAccetpKey() {
        Random random = new Random();
        Integer number = random.nextInt(800000) + 100000;

        return System.currentTimeMillis() + String.valueOf(number);
    }

    /**
     * 生成电费订单id
     * 格式: 时间+随机数
     * @return
     */
    public static synchronized String genPowerKey() {
        Random random = new Random();
        Integer number = random.nextInt(700000) + 100000;

        return System.currentTimeMillis() + String.valueOf(number);
    }
}
