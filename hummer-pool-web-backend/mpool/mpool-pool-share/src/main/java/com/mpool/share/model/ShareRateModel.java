package com.mpool.share.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: stephen
 * @Date: 2019/6/1 16:02
 */

@Data
@TableName("share_rate")
public class ShareRateModel {
    private String day;

    private Double rate;

    private Long diff;

    private Date createTime;
}
