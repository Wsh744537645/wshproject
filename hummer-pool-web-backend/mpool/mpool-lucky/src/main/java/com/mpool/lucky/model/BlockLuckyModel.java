package com.mpool.lucky.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mpool.common.util.DateUtil;
import com.mpool.lucky.serializer.Date2LongSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: stephen
 * @Date: 2019/5/8 11:43
 */

@Data
public class BlockLuckyModel {
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date creatAt;
    private Double lucky;
    private Double exceptBlock;
    private Double realBlock;

    @JsonProperty("time")
    public String getTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return format.format(DateUtil.addHour(creatAt, 8));
    }

    @JsonProperty("luckynum")
    public double getLuckyNum(){
        Double num = lucky * 100.0D;
        BigDecimal big = new BigDecimal(num).setScale(2, BigDecimal.ROUND_HALF_UP);
        return big.doubleValue();
    }
}
