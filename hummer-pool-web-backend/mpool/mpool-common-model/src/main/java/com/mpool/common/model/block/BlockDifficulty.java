package com.mpool.common.model.block;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("block_difficulty")
@Data
public class BlockDifficulty {
    private Integer height;

    private Date time;

    private BigDecimal difficulty;

    private Integer cur_height;
}
