package com.mpool.common.model.program;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import static com.mpool.common.model.constant.AccountConstant.TABLE_PREFIX;

@TableName(TABLE_PREFIX + "openid")
public class WxOpenid implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 9020204956664763485L;

    /**
     * openid Id
     */
    @TableId
    private int id;

    /**
     * openid
     */
    private String openid;
    /**
     * 主账号userid
     */
    private Long userid;
    /**
     * 绑定时间
     */
    private Date addtime;

    /**
     * 解绑时间
     */
    private Date updatetime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }
}
