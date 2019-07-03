package com.mpool.account.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: stephen
 * @Date: 2019/5/6 10:13
 */

@ApiModel
@Data
public class UserPayModel {

    @ApiModelProperty("用户名")
    @NotNull
    private String username;

    @ApiModelProperty("钱包类型")
    @NotNull
    private String walletType;

    @ApiModelProperty("钱包地址")
    private String walletAddress;
}
