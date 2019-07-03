package com.mpool.share.admin.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: stephen
 * @Date: 2019/6/25 11:26
 */

@TableName("log_product")
@Data
public class LogProduct {
    @TableId
    private Integer id;

    private Integer productId;

    private String type;

    private String decrible;

    private String operateBy;

    private Date createTime;
}
