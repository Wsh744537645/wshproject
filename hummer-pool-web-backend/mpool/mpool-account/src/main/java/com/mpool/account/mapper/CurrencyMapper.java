package com.mpool.account.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.Currency;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CurrencyMapper extends BaseMapper<Currency> {
    @Select("select * from account_currency where type=#{walletType}")
    Currency selectOneByCurrencyName(@Param("walletType") String walletType);
}
