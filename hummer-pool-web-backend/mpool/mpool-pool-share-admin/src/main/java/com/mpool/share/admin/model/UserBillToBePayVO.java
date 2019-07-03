package com.mpool.share.admin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: stephen
 * @Date: 2019/6/19 10:23
 */

@Data
public class UserBillToBePayVO {
    private String paidId;

    private Double payNum;

    private Integer state;

    private String txid;

    private String adminName;

    private Date payAt;

    @JsonIgnore
    private Date createTime;

    @JsonProperty("createTime")
    public Long getCreateTime(){
        return createTime.getTime() / 1000;
    }
}
