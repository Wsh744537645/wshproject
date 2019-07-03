package com.mpool.account.service;

import java.util.List;
import java.util.Map;

import com.mpool.common.model.account.Currency;

public interface CurrencyService extends BaseService<Currency> {

	List<Currency> selectListByEnable();

	Integer selectListByCurrencyName(String walletType);

	Map<String, Object> getCurrency();

	String findCurrencyTypeById(Integer currencyId);

	Currency selectOneByCurrencyName(String walletType);

}
