package com.mpool.common.model.log;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName("log_user_operation")
@Data
public class LogUserOperation {
//    @TableId
//    private Integer id;
    /**
     * 用户类型account用户，admin管理员
     */
    private String userType;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 1用户日志2矿池日志，3系统日志
     */
    private int  logType;
    /**
     * 操作内容描述
     */
    private String content;
    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    /**
     * 币种
     */
    private String currencyName;
}
