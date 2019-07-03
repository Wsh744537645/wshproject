package com.mpool.common.model.share;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: stephen
 * @Date: 2019/6/4 11:48
 */

@TableName("user_invite")
@Data
public class UserInvite {
    private Long inviteUid;

    private Long recommendUid;

    private Integer rate;

    private Date createTime;
}
