package com.mpool.account.mapper.bill;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.bill.UserPay;

@Mapper
public interface UserPayMapper extends BaseMapper<UserPay> {

	List<UserPay> taskBill();

	int subtractionTotalDue(@Param("totalDue") Long totalDue, @Param("id") Integer id, @Param("currencyId") Integer currencyId);

	int additionTotalPaid(@Param("totalPaid") Long totalDue, @Param("id") Integer id, @Param("currencyId") Integer currencyId);

	UserPay findByPuid(@Param("puid") Long puid, @Param("currencyId") Integer currencyId);

	int additionTotalDue(@Param("totalDue") Long totalDue, @Param("puid") Long puid, @Param("currencyId") Integer currencyId);

	List<UserPay> getUserPayByPuid(@Param("list") List<Long> userIds, @Param("currencyId") Integer currencyId);

	List<Map<String, Object>> getUserPayByIdWithoutCid(@Param("ids") List<Long> ids, @Param("currencyId") Integer currencyId);
}
