package com.mpool.common.model.share;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: stephen
 * @Date: 2019/7/1 14:40
 */

@TableName("product_state")
@Data
public class ProductStateModel {
    @TableId
    private Integer id;

    private Integer productId;

    private Integer enabled;

    private Date updateTime;

    private Date createTime;
}
