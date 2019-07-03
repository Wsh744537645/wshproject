package com.mpool.account.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.UserWallet;

@Mapper
public interface UserWalletMapper extends BaseMapper<UserWallet> {
	
}
