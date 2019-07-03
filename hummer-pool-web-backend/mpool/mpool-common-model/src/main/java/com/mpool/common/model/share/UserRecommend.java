package com.mpool.common.model.share;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: stephen
 * @Date: 2019/6/4 11:40
 */

@TableName("user_recommend")
@Data
public class UserRecommend {
    private Long puid;

    /**
     * 邀请码
     */
    private String code;

    /**
     * 返佣费率
     */
    private Integer rate;

    private Date createTime;
}
