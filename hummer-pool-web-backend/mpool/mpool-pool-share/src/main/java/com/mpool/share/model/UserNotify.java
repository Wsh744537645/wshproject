package com.mpool.share.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: stephen
 * @Date: 2019/5/31 9:52
 */

@Data
@TableName("user_notify")
public class UserNotify {
    private Long puid;

    private Integer phoneState;

    private Integer emailState;

    private Integer before7;

    private Integer before1;

    private Integer expired;

    public void setUserNotify(UserNotify notify){
        if(notify.getPhoneState() != null){
            this.phoneState = notify.getPhoneState();
        }

        if(notify.getEmailState() != null){
            this.emailState = notify.getEmailState();
        }

        if(notify.getBefore7() != null){
            this.before7 = notify.getBefore7();
        }

        if(notify.getBefore1() != null){
            this.before1 = notify.getBefore1();
        }

        if(notify.getExpired() != null){
            this.expired = notify.getExpired();
        }

    }
}
