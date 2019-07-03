package com.mpool.account.service;

import java.math.BigDecimal;
import java.util.List;

import com.mpool.common.model.account.Currency;
import com.mpool.common.model.account.CurrencyMiniPay;

public interface CurrencyMiniPayService extends BaseService<CurrencyMiniPay> {

	List<BigDecimal> selectListByCurrencyId(Integer id);

	CurrencyMiniPay getMinOneByCurrencyId(Integer id);

	void checkMinPay(Integer currency, BigDecimal minPay);
}
