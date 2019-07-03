package com.mpool.share.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: stephen
 * @Date: 2019/6/4 11:50
 */

@TableName("recommend_bill_item")
@Data
public class RecommendBillItem {
    private String billId;

    private Long recommendUid;

    private Long inviteUid;

    private Date createTime;
}
