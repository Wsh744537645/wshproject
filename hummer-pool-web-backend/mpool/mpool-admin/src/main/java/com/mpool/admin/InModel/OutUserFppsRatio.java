package com.mpool.admin.InModel;

public class OutUserFppsRatio {
    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 子账号名
     */
    private String username;
    /**
     * 用户类别 ：0会员1基石',
     */
    private Integer usreGroup;
    /**
     * 费率
     */
    private Integer fppsRate;
    /**
     * 挖矿模式 1 FPPS
     */
    private Integer payMode;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUsreGroup() {
        return usreGroup;
    }

    public void setUsreGroup(Integer usreGroup) {
        this.usreGroup = usreGroup;
    }

    public Integer getFppsRate() {
        return fppsRate;
    }

    public void setFppsRate(Integer fppsRate) {
        this.fppsRate = fppsRate;
    }

    public Integer getPayMode() {
        return payMode;
    }

    public void setPayMode(Integer payMode) {
        this.payMode = payMode;
    }
}
