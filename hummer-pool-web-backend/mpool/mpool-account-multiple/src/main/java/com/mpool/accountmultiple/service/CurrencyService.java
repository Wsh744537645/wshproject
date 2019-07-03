package com.mpool.accountmultiple.service;

import com.mpool.common.model.account.Currency;

import java.util.List;
import java.util.Map;

public interface CurrencyService extends BaseService<Currency> {

	List<Currency> selectListByEnable();

	Integer selectListByCurrencyName(String walletType);

	Map<String, Object> getCurrency();

	String findCurrencyTypeById(Integer currencyId);

}
