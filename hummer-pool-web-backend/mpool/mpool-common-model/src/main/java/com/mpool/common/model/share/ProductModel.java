package com.mpool.common.model.share;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: stephen
 * @Date: 2019/5/23 12:05
 */

@Data
@TableName("product")
public class ProductModel {
    @TableId
    private Integer productId;

    private String productName;

    /**
     * 矿机ID
     */
    private Integer miningId;

    /**
     * 可购买最小算力
     */
    private Long minAccept;

    /**
     * 算力每TH/s的美元价格
     */
    private Double acceptFee;

    /**
     * 电费:每1算力的美元价格
     */
    private Double powerFee;

    /**
     * 周期
     */
    private Integer period;

    /**
     * 币种id
     */
    private Integer currencyId;

    private String currencyName;

    /**
     * 算力库存
     */
    private Long stock;

    /**
     * 电费价格 美元/度
     */
    private Float powerPrice;

    /**
     * 生效时间
     */
    private Date workTime;

    /**
     * 上架时间
     */
    private Date shelfTime;

    /**
     * 下架时间
     */
    private Date obtainedTime;

    private Date createTime;

    private Date updateTime;
}
