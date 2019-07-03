package com.mpool.share.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import static com.mpool.common.model.constant.AccountConstant.TABLE_PREFIX;

/**
 * @Author: stephen
 * @Date: 2019/5/16 15:41
 */
@TableName(TABLE_PREFIX + "user")
@Data
public class User implements Serializable {


    private static final long serialVersionUID = -178485309640459542L;

    /**
     * 用户Id
     */
    @TableId
    private Long userId;

    /**
     * 登录名字
     */
    private String username;

    /**
     * 安全码
     */
    private String loginSecret;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户手机
     */
    private String userPhone;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户名称
     */
    private String nickName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后一次登录IP
     */
    private String lastIp;

    /**
     * 最后一次登录时间
     */
    private Date lastTime;

    /**
     * 用户来源
     */
    private String userFrom;

    /**
     * 用户头像
     */
    private String userPhoto;


    /**
     * 微信唯一标示openid
     */
    private String openid;

    /**
     * 语言
     */
    private String langType;


    /**
     * 推荐人 不为数据库字段false
     */
    @TableField(exist = false)
    private String recommendName;
}