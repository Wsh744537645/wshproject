package com.mpool.common.model.share;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: stephen
 * @Date: 2019/5/29 17:49
 */

@Data
@TableName("user_pay")
public class UserPayModel {
    private Integer payId;

    private Long puid;

    private Double balance;

    private Double total;

    private Double recommendTotal;

    private String walletAddress;

    private Integer currencyId;

    private Date updateTime;

    private Date createTime;
}
