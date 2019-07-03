package com.mpool.admin.module.bill.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.account.bill.UserPay;

@Mapper
public interface UserPayMapper extends BaseMapper<UserPay> {
	int additionTotalPaid(@Param("totalPaid") Long totalPaid, @Param("id") Integer id);

	IPage<Map<String, Object>> getUserFppsList(IPage<Map<String, Object>> iPage, @Param("group") Integer group,
			@Param("username") String username, @Param("recommendName") String recommendName, @Param("currencyId") Integer currencyId);

    UserPay getUserPayInfo(@Param("puid") Long puid, @Param("currencyId") Integer currencyId);
}
