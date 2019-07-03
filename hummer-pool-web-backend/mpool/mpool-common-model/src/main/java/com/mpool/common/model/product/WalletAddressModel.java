package com.mpool.common.model.product;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: stephen
 * @Date: 2019/5/23 15:52
 */

@Data
@TableName("wallet_address")
public class WalletAddressModel {
    private Integer id;

    private String walletAddress;

    private Integer enabled;
}
