package com.mpool.accountmultiple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.CurrencyMiniPay;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CurrencyMiniPayMapper extends BaseMapper<CurrencyMiniPay>{

	List<BigDecimal> selectListByCurrencyId(Integer id);

}
