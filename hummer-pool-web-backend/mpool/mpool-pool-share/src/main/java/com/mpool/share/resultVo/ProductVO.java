package com.mpool.share.resultVo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: stephen
 * @Date: 2019/5/29 9:37
 */

@Data
public class ProductVO {
    private Integer productId;

    private String productName;

    private Long minAccept;

    private Integer period;

    //@JsonFormat(timezone = "GMT+8", pattern = "yyyyMMddHHmmss")
    @JsonIgnore
    private Date workTime;

    //@JsonIgnore
    private Long stock;

    /**
     * 电费价格 美元/度
     */
    private Float powerPrice;

    //理论年化收益
    private Float shareYear;

    private String miningName;

    private Double acceptFee;

    private Double powerFee;

//    @JsonProperty("workTime")
//    public Long getWorkTime(){
//        return workTime.getTime() / 1000;
//    }
}
