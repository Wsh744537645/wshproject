package com.mpool.accountmultiple.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpool.accountmultiple.mapper.CurrencyMiniPayMapper;
import com.mpool.accountmultiple.service.CurrencyMiniPayService;
import com.mpool.common.model.account.CurrencyMiniPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyMiniPayServiceImpl implements CurrencyMiniPayService {
	@Autowired
	private CurrencyMiniPayMapper currencyMiniPayMapper;

	@Override
	public void insert(CurrencyMiniPay entity) {
		currencyMiniPayMapper.insert(entity);
	}

	@Override
	public void inserts(List<CurrencyMiniPay> entitys) {
		for (CurrencyMiniPay currencyMiniPay : entitys) {
			insert(currencyMiniPay);
		}
	}

	@Override
	public void update(CurrencyMiniPay entity) {
		currencyMiniPayMapper.updateById(entity);
	}

	@Override
	public void delete(Serializable id) {
		currencyMiniPayMapper.deleteById(id);
	}

	@Override
	public CurrencyMiniPay findById(Serializable id) {
		return currencyMiniPayMapper.selectById(id);
	}

	@Override
	public List<BigDecimal> selectListByCurrencyId(Integer id) {
		CurrencyMiniPay currencyMiniPay = new CurrencyMiniPay();
		currencyMiniPay.setCurrencyId(id);
		List<CurrencyMiniPay> selectList = currencyMiniPayMapper.selectList(new QueryWrapper<>(currencyMiniPay));
		if (selectList == null) {
			selectList = new ArrayList<>();
		}
		return selectList.stream().map(CurrencyMiniPay::getMiniPay).collect(Collectors.toList());
	}

	@Override
	public void checkMinPay(Integer currencyId, BigDecimal minPay) {
		CurrencyMiniPay currencyMiniPay = new CurrencyMiniPay();
		currencyMiniPay.setCurrencyId(currencyId);
		currencyMiniPay.setMiniPay(minPay);
		Integer selectCount = currencyMiniPayMapper.selectCount(new QueryWrapper<CurrencyMiniPay>(currencyMiniPay));
		if (selectCount != null && selectCount == 1) {

		} else {
			throw new AccountException("minpay.error", "最小支付不合法");
		}

	}

}
