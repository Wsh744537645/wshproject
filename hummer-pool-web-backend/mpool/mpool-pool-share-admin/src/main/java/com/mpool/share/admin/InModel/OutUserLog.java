package com.mpool.share.admin.InModel;

import java.util.Date;

public class OutUserLog {
    /**
     * 登录人
     */
    private String username;
    /**
     * 操作时间
     */
    private Date createdTime;
    /**
     * 操作内容
     */
    private String content;
    /**
     * 登录ip
     */
    private String ip;
    /**
     * 登录地理位置
     */
    private String addess;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAddess() {
        return addess;
    }

    public void setAddess(String addess) {
        this.addess = addess;
    }
}
