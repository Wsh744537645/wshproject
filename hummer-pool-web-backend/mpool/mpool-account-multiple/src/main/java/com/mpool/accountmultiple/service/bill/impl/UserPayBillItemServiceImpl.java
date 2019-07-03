package com.mpool.accountmultiple.service.bill.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.accountmultiple.mapper.bill.UserPayBillItemMapper;
import com.mpool.accountmultiple.mapper.fpps.UserFppsRecordMapper;
import com.mpool.mpoolaccountmultiplecommon.model.export.PayRecord;
import com.mpool.accountmultiple.service.bill.UserPayBillItemService;
import com.mpool.common.model.account.bill.UserPayBillItem;
import com.mpool.common.model.account.fpps.UserFppsRecord;
import com.mpool.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserPayBillItemServiceImpl implements UserPayBillItemService {
	@Autowired
	private UserPayBillItemMapper userPayBillItemMapper;
	@Autowired
	private UserFppsRecordMapper userFppsRecordMapper;

	@Override
	public IPage<Map<String, Object>> getPayRecord(IPage<Map<String, Object>> page, UserPayBillItem wallet) {
		return userPayBillItemMapper.getPayRecord(page, wallet);
	}

	@Override
	public String getUserPayBtc(Long puid) {
		return userPayBillItemMapper.getUserPayBtc(puid);
	}

	@Override
	public List<UserPayBillItem> getUserPayInPayment(List<Long> userIds) {

		return userPayBillItemMapper.getUserPayInPayment(userIds);
	}

	@Override
	public Long getUserPayYesterday(Long userId) {
		Date yesterday = DateUtil.addDay(new Date(), -1);
		List<UserPayBillItem> userPayYesterdays = userPayBillItemMapper.getUserPayYesterday(userId, yesterday);
		long result = 0;
		for (UserPayBillItem userPayBillItem : userPayYesterdays) {
			long item;
			if (userPayBillItem.getPayBtc() == null) {
				item = 0;
			} else {
				item = userPayBillItem.getPayBtc();
			}

			result += item;
		}
		return result;
	}

	@Override
	public Map<Long, Long> getUsersPayYesterday(List<Long> userIds) {
		Date yesterday = DateUtil.addDay(new Date(), -1);
		List<UserPayBillItem> userPayYesterdays = userPayBillItemMapper.getUsersPayYesterday(userIds, yesterday);
		Map<Long, Long> result = new HashMap<>();
		for (UserPayBillItem userPayBillItem : userPayYesterdays) {
			Long puid = userPayBillItem.getPuid();
			Long payBtc = userPayBillItem.getPayBtc();
			result.put(puid, payBtc);
		}
		return result;
	}

	@Override
	public IPage<UserFppsRecord> getIncomeRecord(IPage<UserFppsRecord> page, Long userId) {
		IPage<UserFppsRecord> userFppsRecords = userFppsRecordMapper.getUserFppsRecordByPuid(page, userId);

		return userFppsRecords;
	}

	@Override
	public List<List<Object>> getPayRecordExport(UserPayBillItem wallet) {
		List<List<Object>> list = new ArrayList<>();
		List<PayRecord> payRecordExport = userPayBillItemMapper.getPayRecordExport(wallet.getPuid());
		for (PayRecord map : payRecordExport) {
			ArrayList<Object> arrayList = new ArrayList<>();
			arrayList.add(map.getTxid());
			arrayList.add(map.getPayAt());
			arrayList.add(map.getPayBtc());
			list.add(arrayList);
		}
		return list;
	}
}
