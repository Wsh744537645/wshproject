package com.mpool.account.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.mpool.account.model.subaccount.SubAccountList;
import com.mpool.common.model.account.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper extends BaseMapper<User> {

	User findByUsername(@Param("username") String username);

	User findBymail(@Param("mail") String mail);

	User findByPhone(@Param("phone") String phone);

	List<User> findSubAccount(@Param("userId") String userId);

	List<User> findSubAccountByCurrencyId(@Param("userId") Long userId, @Param("currencyId") Integer currencyId);

	List<Map<String, Object>> findSubAccountByMap(IPage<Map<String, Object>> page,
			@Param(Constants.WRAPPER) Wrapper<User> queryWrapper);

	/**
	 * 查询子账号
	 * 
	 * @param page
	 * @param userParam
	 * @return
	 */
	IPage<SubAccountList> findUserByMaster(IPage<SubAccountList> page, @Param("user") User userParam, @Param("currencyId") Integer currencyId);

	User findById(@Param("userId")Long userId);

	IPage<User> selectUserPageByPayCurrencyId(IPage<User> iPage, @Param("masterId") Long masterId);

}
