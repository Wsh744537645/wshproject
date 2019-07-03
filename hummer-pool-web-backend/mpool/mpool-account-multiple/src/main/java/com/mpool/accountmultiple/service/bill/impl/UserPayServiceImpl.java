package com.mpool.accountmultiple.service.bill.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpool.accountmultiple.utils.AccountUtils;
import com.mpool.common.properties.MultipleProperties;
import com.mpool.mpoolaccountmultiplecommon.constant.Constant;
import com.mpool.mpoolaccountmultiplecommon.exception.AccountException;
import com.mpool.mpoolaccountmultiplecommon.exception.ExceptionCode;
import com.mpool.accountmultiple.mapper.bill.UserPayBillItemMapper;
import com.mpool.accountmultiple.mapper.bill.UserPayMapper;
import com.mpool.accountmultiple.mapper.fpps.FppsRatioDayMapper;
import com.mpool.accountmultiple.mapper.log.WalletLogMapper;
import com.mpool.accountmultiple.mapper.pool.StatsUsersDayMapper;
import com.mpool.mpoolaccountmultiplecommon.model.WalletModel;
import com.mpool.accountmultiple.service.CurrencyService;
import com.mpool.accountmultiple.service.EmailService;
import com.mpool.accountmultiple.service.UserRegionSerice;
import com.mpool.accountmultiple.service.UserWalletPayTypeService;
import com.mpool.accountmultiple.service.bill.UserPayService;
import com.mpool.common.CoinValidationUtil;
import com.mpool.common.model.account.UserWalletPayType;
import com.mpool.common.model.account.bill.UserPay;
import com.mpool.common.model.account.bill.UserPayBillItem;
import com.mpool.common.model.account.fpps.FppsRatioDay;
import com.mpool.common.model.account.log.WalletLog;
import com.mpool.common.model.pool.StatsUsersDay;
import com.mpool.common.util.DateUtil;
import com.mpool.common.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserPayServiceImpl implements UserPayService {

	private static final Logger log = LoggerFactory.getLogger(UserPayServiceImpl.class);

	@Autowired
	private UserPayMapper btcUserPayMapper;
	@Autowired
	private WalletLogMapper walletLogMapper;
	@Autowired
	private UserRegionSerice userRegionSerice;
	@Autowired
	private UserWalletPayTypeService userWalletPayTypeService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private UserPayBillItemMapper userPayBillItemMapper;
	@Autowired
	private StatsUsersDayMapper statsUsersDayMapper;
	@Autowired
	private FppsRatioDayMapper fppsRatioDayMapper;
	@Autowired
	private MultipleProperties properties;

	@Override
	public void insert(UserPay entity) {
		btcUserPayMapper.insert(entity);
	}

	@Override
	public void update(UserPay entity) {
		Long puid = entity.getPuid();
		Integer currencyId = entity.getCurrencyId();
		UserPay userPayWrapper = new UserPay();
		userPayWrapper.setPuid(puid);
		userPayWrapper.setCurrencyId(currencyId);
		
		UserPay userPay = btcUserPayMapper.selectOne(new QueryWrapper<UserPay>(userPayWrapper));
		if (entity.getWalletAddress() != null && !entity.getWalletAddress().equals(userPay.getWalletAddress())) {
			if (!CoinValidationUtil.bitCoinAddressValidate(entity.getWalletAddress())) {
				throw new AccountException(ExceptionCode.coin_error);
			} else {
				log.debug("update wallet addres");
				userPay.setUpdateTime(new Date());
			}
		}

		WalletLog walletLog = new WalletLog();
		Long minPay = entity.getMinPay();
		String walletAddr = entity.getWalletAddress();

		walletLog.setOldData(JSONUtil.toJson(userPay));
		int flg = 0;
		if (!userPay.getMinPay().equals(minPay)) {
			userPay.setMinPay(minPay);
			flg++;
		}
		if (walletAddr != null && !walletAddr.equals(userPay.getWalletAddress())) {
			userPay.setWalletAddress(walletAddr);
			flg++;
		}
		if (!userPay.getCurrencyId().equals(currencyId)) {
			userPay.setCurrencyId(currencyId);
			flg++;
		}

		// btc type
		if (flg == 0) {
			return;
		}

		// 日志
		walletLog.setPuid(puid);
		walletLog.setTagetId(userPay.getId());
		walletLog.setUpdateBy(AccountUtils.getUser().getUserId());
		walletLog.setUpdateTime(new Date());
		// update
		walletLog.setNewData(JSONUtil.toJson(userPay));

		btcUserPayMapper.updateById(userPay);

		walletLogMapper.insert(walletLog);
	}

	@Override
	public void delete(Serializable id) {
		btcUserPayMapper.deleteById(id);
	}

	@Override
	public UserPay findById(Serializable id) {
		return btcUserPayMapper.selectById(id);
	}

	@Override
	public WalletModel findWallets(Long userId) {
		UserPay entity = new UserPay();
		entity.setPuid(userId);
		UserPay selectList = btcUserPayMapper.selectOne(new QueryWrapper<UserPay>(entity));
		return new WalletModel(selectList);
	}

	@Override
	public void inserts(List<UserPay> entitys) {
		// TODO Auto-generated method stub

	}

	@Override
	public WalletModel getWalletInfo(Long userId) {
		UserPay entity = new UserPay();
		entity.setPuid(userId);
		UserPay userPay = btcUserPayMapper.selectOne(new QueryWrapper<UserPay>(entity));
		if (userPay == null) {
			throw new AccountException(ExceptionCode.user_wallet_empty);
		}
		WalletModel model = new WalletModel();
		String regionName = userRegionSerice.findRegionNameByUserId(userId);
		model.setRegionName(regionName);
		UserWalletPayType userWalletPayType = userWalletPayTypeService.findById(userPay.getPayModel());
		model.setPayType(userWalletPayType.getType());
		String walletType = currencyService.findCurrencyTypeById(userPay.getCurrencyId());
		model.setWalletType(walletType);
		BigDecimal minPay = BigDecimal.valueOf(userPay.getMinPay()).divide(BigDecimal.valueOf(Constant.MIN_PAY));
		model.setMiniPay(minPay);
		model.setWalletAddr(userPay.getWalletAddress());
		model.setWalletId(userPay.getId());
		return model;
	}

	@Override
	public UserPay getPayHistory(Long userId) {
		UserPay entity = new UserPay();
		entity.setPuid(userId);
		UserPay selectOne = btcUserPayMapper.selectOne(new QueryWrapper<UserPay>(entity));
		if (selectOne == null) {
			log.warn("user wallet is null userId={}", userId);
			return new UserPay();
		}
		log.debug("user wallet info -> {}", JSONUtil.toJson(selectOne));
		return selectOne;
	}

	@Override
	public BigDecimal getPayCurrent(Long userId) {
		String yyyymMdd = DateUtil.getYYYYMMdd(new Date());
		StatsUsersDay entity = new StatsUsersDay();
		entity.setPuid(userId);
		entity.setDay(Long.parseLong(yyyymMdd));

		//获得今日用户一天的数据
		StatsUsersDay selectOne = statsUsersDayMapper.selectOne(new QueryWrapper<StatsUsersDay>(entity));
		if (selectOne == null) {
			return BigDecimal.valueOf(0);
		}
		//今日预收：以今天为基准统计ratio最近432个值的平均值得出的系数
		//预计获得的btc数量
		Long earn = selectOne.getEarn();
		FppsRatioDay ratio = fppsRatioDayMapper.getRatio(Integer.parseInt(yyyymMdd));
		Float f =ratio.getRatio();
		if (ratio == null) {
			f = 1.01f;
		}else {
			if (ratio.getNewRatio()==0.0f){
				f=ratio.getRatio();

			}else {
				f=ratio.getNewRatio();
			}

		}
		return BigDecimal.valueOf(earn).multiply(BigDecimal.valueOf(f));
	}

	@Override
	@Transactional
	public void taskBill() {
		log.info("begin task bill ->>>>>>>>>>>>> {} >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", new Date());
		List<UserPay> list = btcUserPayMapper.taskBill(properties.getId());
		log.debug("bill data -> {}", JSONUtil.toJson(list));
		List<UserPayBillItem> billItems = new ArrayList<UserPayBillItem>();
		Date date = new Date();
		String datestr = DateUtil.getYYYYMMdd(date);
		if (list != null && !list.isEmpty()) {
			for (UserPay userPay : list) {
				Long totalDue = userPay.getTotalDue();
				Integer id = userPay.getId();
				Long puid = userPay.getPuid();
				String walletAddress = userPay.getWalletAddress();
				int row = btcUserPayMapper.subtractionTotalDue(totalDue, id, properties.getId());
				if (row != 1) {

				}
				UserPayBillItem e = new UserPayBillItem();
				e.setBillNumber("0");
				e.setCteateAt(date);
				e.setPayBtc(totalDue);
				e.setPuid(puid);
				e.setDay(Long.parseLong(datestr));
				e.setWalletAddress(walletAddress);
				billItems.add(e);
			}
			log.debug("billItems data -> {}", JSONUtil.toJson(billItems));
			userPayBillItemMapper.inserts(billItems);
		} else {

		}
		log.info("end task bill ->>>>>>>>>>>>> {} >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", new Date());
	}

	@Override
	public List<UserPay> getUserPayByPuid(List<Long> userIds) {
		return btcUserPayMapper.getUserPayByPuid(userIds, properties.getId());
	}

}
