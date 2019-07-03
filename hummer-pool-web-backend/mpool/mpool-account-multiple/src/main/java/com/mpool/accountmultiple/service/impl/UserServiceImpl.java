package com.mpool.accountmultiple.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.accountmultiple.mapper.UserMapper;
import com.mpool.accountmultiple.service.UserService;
import com.mpool.common.model.account.User;
import com.mpool.common.properties.MultipleProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	/**
	 * 用户表操作
	 */
	@Autowired
	private UserMapper usersMapper;
	@Autowired
	private MultipleProperties multipleProperties;

	@Override
	public User findByUsername(String username) {
		return usersMapper.findByUsername(username);
	}

	@Override
	public void insert(User entity) {
		usersMapper.insert(entity);
	}

	@Override
	public void inserts(List<User> entitys) {
		for (User user : entitys) {
			insert(user);
		}
	}

	@Override
	public void update(User entity) {
		usersMapper.updateById(entity);
	}

	@Override
	public void delete(Serializable id) {
		usersMapper.deleteById(id);
	}

	@Override
	public User findById(Serializable id) {
		return usersMapper.selectById(id);
	}

	@Override
	public List<User> findSubAccount(String userId) {
		if (userId != null) {
			return usersMapper.findSubAccount(userId);
		} else {
			return null;
		}

	}

	@Override
	public List<User> findSubAccountByCurrencyId(Long userId) {
		if (userId != null) {
			return usersMapper.findSubAccountByCurrencyId(userId, multipleProperties.getId());
		} else {
			return null;
		}
	}

	@Override
	public IPage<User> selectUserPage(IPage<User> iPage, User user) {
		return usersMapper.selectPage(iPage, new QueryWrapper<User>(user));
	}

	@Override
	public IPage<User> selectUserPageByPayCurrencyId(IPage<User> iPage, Long masterId) {
		return usersMapper.selectUserPageByPayCurrencyId(iPage, masterId, multipleProperties.getId());
	}
}
