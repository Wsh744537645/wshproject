package com.mpool.share.mapper.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.product.WalletAddressModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/23 15:55
 */

@Mapper
public interface WalletAddressMapper extends BaseMapper<WalletAddressModel> {

    List<WalletAddressModel> getOne();

    int updateEnabled(@Param("id") Integer id, @Param("enabled") Integer enabled);

    void updateEnabledBatch(@Param("walletAddressList") List<String> walletAddressList, @Param("enabled") Integer enabled);

    List<WalletAddressModel> getHadBalanceWalletAddressList();

    void inserts(@Param("list") List<String> list);
}
