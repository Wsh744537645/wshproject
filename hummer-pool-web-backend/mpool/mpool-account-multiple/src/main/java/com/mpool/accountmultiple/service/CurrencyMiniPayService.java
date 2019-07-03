package com.mpool.accountmultiple.service;

import com.mpool.common.model.account.CurrencyMiniPay;

import java.math.BigDecimal;
import java.util.List;

public interface CurrencyMiniPayService extends BaseService<CurrencyMiniPay> {

	List<BigDecimal> selectListByCurrencyId(Integer id);

	void checkMinPay(Integer currency, BigDecimal minPay);
}
