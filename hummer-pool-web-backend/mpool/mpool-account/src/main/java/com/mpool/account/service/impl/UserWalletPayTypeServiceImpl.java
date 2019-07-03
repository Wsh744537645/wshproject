package com.mpool.account.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpool.account.exception.AccountException;
import com.mpool.account.mapper.UserWalletPayTypeMapper;
import com.mpool.account.service.UserWalletPayTypeService;
import com.mpool.common.model.account.UserWalletPayType;

@Service
public class UserWalletPayTypeServiceImpl implements UserWalletPayTypeService {
	@Autowired
	private UserWalletPayTypeMapper userWalletPayTypeMapper;

	@Override
	public void insert(UserWalletPayType entity) {
		userWalletPayTypeMapper.insert(entity);
	}

	@Override
	public void inserts(@NonNull List<UserWalletPayType> entitys) {
		for (UserWalletPayType userWalletPayType : entitys) {
			insert(userWalletPayType);
		}
	}

	@Override
	public void update(UserWalletPayType entity) {
		userWalletPayTypeMapper.updateById(entity);
	}

	@Override
	public void delete(Serializable id) {
		userWalletPayTypeMapper.deleteById(id);
	}

	@Override
	public UserWalletPayType findById(Serializable id) {
		return userWalletPayTypeMapper.selectById(id);
	}

	@Override
	public List<String> selectListByEnable() {
		UserWalletPayType entity = new UserWalletPayType();
		entity.setEnable(true);
		List<UserWalletPayType> selectList = userWalletPayTypeMapper
				.selectList(new QueryWrapper<UserWalletPayType>(entity));
		return selectList.stream().map(UserWalletPayType::getType).collect(Collectors.toList());
	}

	@Override
	public Integer payIdByType(String payType) {
		UserWalletPayType entity = new UserWalletPayType();
		entity.setType(payType);
		entity.setEnable(true);
		UserWalletPayType selectOne = userWalletPayTypeMapper.selectOne(new QueryWrapper<UserWalletPayType>(entity));
		if (selectOne == null || payType == null) {
			throw new AccountException("0000002", "支付类型错误");
		}
		return selectOne.getId();
	}

}
