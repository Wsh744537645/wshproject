package com.mpool.admin.module.bill.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.mpool.admin.module.account.mapper.UserMapper;
import com.mpool.admin.module.system.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mpool.admin.module.bill.mapper.PoolRateServiceMapper;
import com.mpool.admin.module.bill.service.PoolRateService;
import com.mpool.admin.utils.AdminSecurityUtils;

@Service
public class PoolRateServiceImpl implements PoolRateService {
	@Autowired
	private PoolRateServiceMapper poolRateServiceMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private CurrencyService currencyService;

	@Override
	@Transactional
	public void updatePoolRate(Float poolRate) {
		// 找到 矿池当前生效的费率
		int newRate = BigDecimal.valueOf(poolRate).multiply(BigDecimal.valueOf(1000)).intValue();
		String username = AdminSecurityUtils.getUser().getUsername();
		Date date = new Date();

		// 执行顺序不能乱
		// 更新默认费率的用户为新费率
		List<Long> puids = poolRateServiceMapper.getVipUser();
		if (!puids.isEmpty()) {
			poolRateServiceMapper.updateDefaultUserRate(newRate, puids, currencyService.getCurCurrencyId());
		}

		// 结束掉当前生效的费率
		poolRateServiceMapper.updatePoolRateByEndTime(date, username, currencyService.getCurCurrencyId());
		// 插入新的费率
		poolRateServiceMapper.insertNewRate(date, newRate, username, currencyService.getCurCurrencyId());
		// 修改用户的等级
//		Integer poolDefaultRate = poolRateServiceMapper.getPoolDefaultRate();
		List<Long> userIds = poolRateServiceMapper.getDefaultRateByUser(newRate, currencyService.getCurCurrencyId());
		if (!userIds.isEmpty()) {
			poolRateServiceMapper.updateUserVipLevel(userIds);
		}


	}

}
