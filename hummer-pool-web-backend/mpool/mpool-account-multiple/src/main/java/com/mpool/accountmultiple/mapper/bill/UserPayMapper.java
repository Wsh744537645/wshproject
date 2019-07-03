package com.mpool.accountmultiple.mapper.bill;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.bill.UserPay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserPayMapper extends BaseMapper<UserPay> {

	List<UserPay> taskBill(@Param("currencyId") int currencyId);

	int subtractionTotalDue(@Param("totalDue") Long totalDue, @Param("id") Integer id, @Param("currencyId") Integer currencyId);

	int additionTotalPaid(@Param("totalPaid") Long totalDue, @Param("id") Integer id, @Param("currencyId") Integer currencyId);

	UserPay findByPuid(@Param("puid") Long puid, @Param("currencyId") Integer currencyId);

	int additionTotalDue(@Param("totalDue") Long totalDue, @Param("puid") Long puid, @Param("currencyId") Integer currencyId);

	List<UserPay> getUserPayByPuid(@Param("list") List<Long> userIds, @Param("currencyId") Integer currencyId);

}
