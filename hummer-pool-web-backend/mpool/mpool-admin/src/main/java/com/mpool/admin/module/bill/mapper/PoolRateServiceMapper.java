package com.mpool.admin.module.bill.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import io.shardingsphere.core.parsing.parser.clause.InsertColumnsClauseParser;

@Mapper
public interface PoolRateServiceMapper {

	void updateDefaultUserRate(@Param("newRate") int newRate, @Param("puids") List<Long> puids, @Param("currencyId") Integer currencyId);

	@Update("UPDATE pool_rate SET end_time = #{date}, update_by = #{username} WHERE currency_id=#{currencyId} AND end_time IS NULL")
	void updatePoolRateByEndTime(@Param("date") Date date, @Param("username") String username, @Param("currencyId") Integer currencyId);

	@Insert("INSERT INTO pool_rate (rate, start_time, update_by, currency_id ) VALUES (#{newRate}, #{date}, #{username}, #{currencyId})")
	void insertNewRate(@Param("date") Date date, @Param("newRate") Integer newRate, @Param("username") String username, @Param("currencyId") Integer currencyId);

	void updateUserVipLevel(@Param("userIds") List<Long> userIds);

	/**
	 * 获取所有会员
	 * 
	 * @return
	 */
	@Select("SELECT user_id FROM account_user WHERE user_group = 0")
	List<Long> getVipUser();

	/**
	 * 获取矿池默认费率
	 * 
	 * @return
	 */
	@Select("SELECT rp.rate  FROM pool_rate rp WHERE rp.currency_id=#{currencyId} AND rp.end_time IS NULL")
	Integer getPoolDefaultRate(@Param("currencyId") Integer currencyId);

	/**
	 * 获得默认费率的用户
	 * 
	 * @param rate
	 * @return
	 */
	@Select(" SELECT puid FROM user_pay up WHERE up.fpps_rate = #{rate} AND up.currency_id=#{currencyId}")
	List<Long> getDefaultRateByUser(Integer rate, @Param("currencyId") Integer currencyId);

}
