package com.mpool.share.model;

import lombok.Data;

/**
 * @Author: stephen
 * @Date: 2019/6/5 16:15
 */

@Data
public class NotifyResultModel {
    private Long puid;

    private Integer phoneState;

    private Integer emailState;

    private Integer before7;

    private Integer before1;

    private Integer expired;

    private String phone;

    private String email;

    public boolean isPhoneNotify(){
        return phoneState.equals(1);
    }

    public boolean isEmailNotify(){
        return emailState.equals(1);
    }

    public boolean isBefore7Notify(){
        return before7.equals(1);
    }

    public boolean isBefore1Notify(){
        return before1.equals(1);
    }

    public boolean isExpiredNotify(){
        return expired.equals(1);
    }
}
