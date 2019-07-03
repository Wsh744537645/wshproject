package com.mpool.account.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.CurrencyMiniPay;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CurrencyMiniPayMapper extends BaseMapper<CurrencyMiniPay>{

	List<BigDecimal> selectListByCurrencyId(Integer id);

	@Select("select * from account_currency_mini_pay where currency_id=#{currencyId} ORDER BY mini_pay LIMIT 1")
	CurrencyMiniPay selectMinOneByCurrencyId(@Param("currencyId") Integer currencyId);
}
