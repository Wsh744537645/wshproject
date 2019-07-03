package com.mpool.account.mapper.bill;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.account.model.export.PayRecord;
import com.mpool.common.model.account.bill.UserPayBillItem;

@Mapper
public interface UserPayBillItemMapper extends BaseMapper<UserPayBillItem> {

	IPage<Map<String, Object>> getPayRecord(IPage<Map<String, Object>> page, @Param("payItem") UserPayBillItem payItem);

	int inserts(@Param("list") List<UserPayBillItem> billItems);

	String getUserPayBtc(@Param("puid") Long puid);

	List<UserPayBillItem> getUserPayInPayment(@Param("list") List<Long> userIds);

	List<UserPayBillItem> getUserPayYesterday(@Param("userId") Long userId, @Param("yesterday") Date yesterday);

	List<UserPayBillItem> getUsersPayYesterday(@Param("userIds") List<Long> userIds,
			@Param("yesterday") Date yesterday);

	List<PayRecord> getPayRecordExport(@Param("puid") Long puid);
}
