package com.mpool.admin.module.bill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.exception.AdminException;
import com.mpool.admin.module.account.mapper.UserMapper;
import com.mpool.admin.module.bill.mapper.PoolRateServiceMapper;
import com.mpool.admin.module.bill.mapper.UserPayMapper;
import com.mpool.admin.module.bill.service.UserPayService;
import com.mpool.admin.module.log.mapper.FppsRatelogMapper;
import com.mpool.admin.module.system.service.CurrencyService;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.common.model.account.bill.UserPay;
import com.mpool.common.model.account.log.FppsRateLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Service
public class UserPayServiceImpl implements UserPayService {

	private static final Logger log = LoggerFactory.getLogger(UserPayServiceImpl.class);

	@Autowired
	private UserPayMapper btcUserPayMapper;

	@Autowired
	private FppsRatelogMapper fppsRatelogMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private PoolRateServiceMapper poolRateServiceMapper;
	@Autowired
	private CurrencyService currencyService;

	@Override
	@Transactional
	public void updateFppsRate(Long userId, Float fppsRate) {
		UserPay btcUserPay = new UserPay();
		btcUserPay.setPuid(userId);
		UserPay selectOne = btcUserPayMapper.selectOne(new QueryWrapper<UserPay>(btcUserPay));
		if (selectOne == null) {
			throw new AdminException("wallet.not.found", "没有找到用户的钱包!");
		}
		fppsRate = fppsRate * 1000;
		FppsRateLog entity = new FppsRateLog();
		if (fppsRate % 1 == 0) {

			entity.setOldFppsRate(selectOne.getFppsRate());
			selectOne.setFppsRate((int) (fppsRate + 0));
			entity.setNewFppsRate(selectOne.getFppsRate());
			entity.setPuid(userId);
			entity.setTagetId(selectOne.getId());
			entity.setUpdateBy(AdminSecurityUtils.getUser().getUserId());//获取当前登录的管理员
			entity.setUpdateTime(new Date());

		} else {
			throw new AdminException("error.fpps.rate", "费率支持小数点后面3位");
		}
		btcUserPayMapper.updateById(selectOne);
		fppsRatelogMapper.insert(entity);

		//修改费率时如果修改的费率跟默认的一样则未会员，否则为基石
//		int nRate = BigDecimal.valueOf(fppsRate).multiply(BigDecimal.valueOf(1000)).intValue();
		int nRate = BigDecimal.valueOf(fppsRate).intValue();
		log.debug("传入的修改费率:"+nRate);
		Integer rate = poolRateServiceMapper.getPoolDefaultRate(currencyService.getCurCurrencyId());
		if (rate == nRate ){
			userMapper.updateUserGroupByIdMember(userId);//费率修改后更新会员
		}else {
			userMapper.updateUserGroupById(userId);//费率修改后更新会员为基石
		}


	}

	@Override
	public IPage<Map<String, Object>> getUserFppsList(IPage<Map<String, Object>> iPage, Integer group, String username,
			String recommendName) {

		IPage<Map<String, Object>> pageList = btcUserPayMapper.getUserFppsList(iPage, group, username, recommendName, currencyService.getCurCurrencyId());
		return pageList;
	}

}
