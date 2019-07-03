package com.mpool.common.model.share;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: stephen
 * @Date: 2019/5/23 12:14
 */

@Data
@TableName("mining")
public class MiningModel {
    /**
     * 矿机ID
     */
    private Integer id;

    /**
     * 矿机型号
     */
    private String miningName;

    /**
     * 功耗
     */
    private Double powerConsumption;
}
