package com.mpool.admin.module.system.mapper;

import com.mpool.common.model.account.Currency;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/13 18:20
 */
@Mapper
public interface CurrencyMapper {

    @Select("select * from account_currency")
    List<Currency> getAllCurrency();
}
