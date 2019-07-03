package com.mpool.common.model.account;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

import static com.mpool.common.model.constant.AccountConstant.TABLE_PREFIX;

@TableName(TABLE_PREFIX + "ip_region")
public class IpRegion {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户登录ip地址
     */
    private String ip;
    /**
     * 用户登录城市信息
      */
    private String region;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
