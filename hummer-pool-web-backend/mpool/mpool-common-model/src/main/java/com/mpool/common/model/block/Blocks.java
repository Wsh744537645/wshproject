package com.mpool.common.model.block;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("blocks")
@Data
public class Blocks {
    private Integer height;

    private  Date time;

    private BigDecimal difficulty;

    private Long nonce;
}
