package com.mpool.accountmultiple.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpool.mpoolaccountmultiplecommon.exception.AccountException;
import com.mpool.accountmultiple.mapper.CurrencyMapper;
import com.mpool.accountmultiple.service.CurrencyMiniPayService;
import com.mpool.accountmultiple.service.CurrencyService;
import com.mpool.accountmultiple.service.UserWalletPayTypeService;
import com.mpool.common.model.account.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CurrencyServiceImpl implements CurrencyService {
	@Autowired
	private CurrencyMapper currencyMapper;
	@Autowired
	private CurrencyMiniPayService currencyMiniPayService;
	@Autowired
	private UserWalletPayTypeService userWalletPayTypeService;

	@Override
	public void insert(Currency entity) {
		currencyMapper.insert(entity);

	}

	@Override
	public void inserts(List<Currency> entitys) {
		for (Currency currency : entitys) {
			insert(currency);
		}
	}

	@Override
	public void update(Currency entity) {
		currencyMapper.updateById(entity);
	}

	@Override
	public void delete(Serializable id) {
		currencyMapper.deleteById(id);
	}

	@Override
	public Currency findById(Serializable id) {
		return currencyMapper.selectById(id);
	}

	@Override
	public List<Currency> selectListByEnable() {
		Currency entity = new Currency();
		entity.setEnable(true);
		return currencyMapper.selectList(new QueryWrapper<Currency>(entity));
	}

	@Override
	public Integer selectListByCurrencyName(String walletType) {
		Currency entity = new Currency();
		entity.setType(walletType);
		entity.setEnable(true);
		Currency selectOne = currencyMapper.selectOne(new QueryWrapper<Currency>(entity));
		if (selectOne == null) {
			throw new AccountException("currency.type.error", "币种错误!");
		}
		return selectOne.getId();
	}

	@Override
	public Map<String, Object> getCurrency() {
		Map<String, Object> result = new HashMap<>();
		List<Currency> list = this.selectListByEnable();
		if (list == null || list.isEmpty()) {

		}
		Map<String, Object> map = new HashMap<>();
		for (Currency currency : list) {
			List<BigDecimal> bigDecimals = currencyMiniPayService.selectListByCurrencyId(currency.getId());
			map.put(currency.getType(), bigDecimals);
		}
		result.put("walletType", map);
		List<String> values = userWalletPayTypeService.selectListByEnable();
		result.put("payType", values);
		return result;
	}

	@Override
	public String findCurrencyTypeById(Integer currencyId) {
		return currencyMapper.selectById(currencyId).getType();
	}

}
