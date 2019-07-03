package com.mpool.share.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: stephen
 * @Date: 2019/6/5 12:03
 */

@Data
@TableName("user_power_spend")
public class PowerSpendModel {
    private Integer id;

    private Long puid;

    private String billId;

    private Double powerSpend;

    private Date createTime;
}
