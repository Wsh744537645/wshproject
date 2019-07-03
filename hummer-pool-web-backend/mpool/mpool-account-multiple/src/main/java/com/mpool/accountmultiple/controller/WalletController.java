package com.mpool.accountmultiple.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.accountmultiple.aspect.annotation.MultipleCheckCodeEmail;
import com.mpool.accountmultiple.aspect.annotation.MultipleCheckCodeSms;
import com.mpool.accountmultiple.utils.AccountUtils;
import com.mpool.accountmultiple.utils.ParseRequestUtils;
import com.mpool.common.util.GsonUtil;
import com.mpool.mpoolaccountmultiplecommon.exception.AccountException;
import com.mpool.mpoolaccountmultiplecommon.exception.ExceptionCode;
import com.mpool.accountmultiple.mapper.UserMapper;
import com.mpool.mpoolaccountmultiplecommon.model.WalletModel;
import com.mpool.accountmultiple.service.*;
import com.mpool.accountmultiple.service.bill.UserPayBillItemService;
import com.mpool.accountmultiple.service.bill.UserPayService;
import com.mpool.accountmultiple.service.log.UserOperationService;
import com.mpool.common.CoinValidationUtil;
import com.mpool.common.Result;
import com.mpool.common.cache.service.EmailCodeCacheService;
import com.mpool.common.cache.service.PhoneCodeCahceService;
import com.mpool.common.model.account.User;
import com.mpool.common.model.account.bill.UserPay;
import com.mpool.common.model.account.bill.UserPayBillItem;
import com.mpool.common.model.account.fpps.UserFppsRecord;
import com.mpool.common.model.log.LogUserOperation;
import com.mpool.common.util.DateUtil;
import com.mpool.common.util.ExcelUtil;
import com.mpool.common.util.UUIDUtil;
import com.mpool.common.util.page.Page;
import com.mpool.common.util.page.PageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@RestController
@RequestMapping({ "/wallet", "/apis/wallet" })
@Api("钱包管理")
public class WalletController {
	@Autowired
	private UserPayService walletService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private EmailCodeCacheService emailCodeCacheService;
	@Autowired
	private SmsService smsService;
	@Autowired
	private PhoneCodeCahceService phoneCodeCahceService;
	@Autowired
	private CurrencyMiniPayService currencyMiniPayService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private UserPayBillItemService userPayBillItemService;
	@Autowired
	private UserService userService;
	@Autowired
	private ExecutorService executorService;
	@Autowired
	private UserOperationService userOperationService;
	@Autowired
	private UserMapper userMapper;

	@ApiOperation("修改钱包")
	@RequestMapping("/user/wallet/edit")
	@MultipleCheckCodeEmail(prefix = "edit:wallet")
	public Result editUserWallet() {
		WalletModel walletModel = ParseRequestUtils.getObjectValue(WalletModel.class);
		Long userId = ParseRequestUtils.getLongValueByKey("userId");

		UserPay userPay = new UserPay();

		userPay.setId((walletModel.getWalletId()));

		BigDecimal minPay = walletModel.getMiniPay();

		String walletType = walletModel.getWalletType();

		Integer currencyId = currencyService.selectListByCurrencyName(walletType);

		currencyMiniPayService.checkMinPay(currencyId, minPay);

		userPay.setMinPay(walletModel.getMiniPay().multiply(BigDecimal.valueOf(Math.pow(10, 8))).longValue());

		userPay.setWalletAddress(walletModel.getWalletAddr());

		userPay.setCurrencyId(currencyId);

		if (userId == null) {
			User user = AccountUtils.getUser();
			userPay.setPuid(user.getUserId());
		} else {
			userPay.setPuid(userId);
		}

		walletService.update(userPay);

		return Result.ok(userPay);
	}

	@ApiOperation("获取登录用户的钱包")
	@RequestMapping("/user/wallet/info")
	public Result getUserWallet() {
		User user = AccountUtils.getUser();
		WalletModel walletModel = walletService.getWalletInfo(user.getUserId());
		walletModel.setEmail(user.getUserEmail());
		walletModel.setPhone(user.getUserPhone());
		return Result.ok(walletModel);
	}

	@ApiOperation("修改钱包获取验证码")
	@RequestMapping("/send/email")
	public Result sendEmail() {
		User user = AccountUtils.getUser();
		String sessionId = SecurityUtils.getSubject().getSession().getId().toString();

		String code = UUIDUtil.generateNumber(6);
		String key = "edit:wallet" + emailCodeCacheService.builderKey(sessionId, code);
		emailCodeCacheService.putCode(key, code);
		emailService.sendSimple(user.getUserEmail(), "修改钱包验证码", code);
		return Result.ok(code);
	}

	@ApiOperation("修改钱包地址")
	@RequestMapping("/user/wallet/edit/phone")
	@MultipleCheckCodeSms(prefix = "edit:wallet")
	public Result editUserWalletPhone() {
		WalletModel walletModel = ParseRequestUtils.getObjectValue(WalletModel.class);
		Long userId = ParseRequestUtils.getLongValueByKey("userId");

		UserPay userPay = new UserPay();

		userPay.setId((walletModel.getWalletId()));

		BigDecimal minPay = walletModel.getMiniPay();

		String walletType = walletModel.getWalletType();

		Integer currencyId = currencyService.selectListByCurrencyName(walletType);

		currencyMiniPayService.checkMinPay(currencyId, minPay);

		userPay.setMinPay(walletModel.getMiniPay().multiply(BigDecimal.valueOf(Math.pow(10, 8))).longValue());

		userPay.setWalletAddress(walletModel.getWalletAddr());

		userPay.setCurrencyId(currencyId);

		if (userId == null) {
			User user = AccountUtils.getUser();
			userPay.setPuid(user.getUserId());
		} else {
			userPay.setPuid(userId);
		}
		walletService.update(userPay);//修改子账号钱包地址
		//记录日志
		executorService.execute(() -> {
			User user = userMapper.findById(userId);
			if (user != null){
				Long masterId = user.getMasterUserId();
				User users = userMapper.findById(masterId);
				if (users !=null){
					LogUserOperation model = new LogUserOperation();
					model.setUserType("account");
					model.setLogType(1);
					model.setCreatedTime(new Date());
					model.setUserId(masterId);
					model.setContent("主用户["+users.getUsername()+"]-修改子账户+["+user.getUsername()+"]+钱包地址操作成功");
					userOperationService.insert(model);
				}
			}
		});
		return Result.ok(userPay);
	}

	@ApiOperation("修改钱包最小支付")
	@RequestMapping("/user/wallet/edit/minPay")
	public Result editUserWalletMinPay() {
		BigDecimal minPay = BigDecimal.valueOf(Double.parseDouble(ParseRequestUtils.getStringByKey("minPay")));
		String walletType = ParseRequestUtils.getStringByKey("walletType");
		Long userId = ParseRequestUtils.getLongValueByKey("userId");

		UserPay userPay = new UserPay();

		Integer currencyId = currencyService.selectListByCurrencyName(walletType);

		currencyMiniPayService.checkMinPay(currencyId, minPay);

		userPay.setMinPay(minPay.multiply(BigDecimal.valueOf(Math.pow(10, 8))).longValue());

		userPay.setCurrencyId(currencyId);

		if (userId == null) {
			User user = AccountUtils.getUser();
			userPay.setPuid(user.getUserId());
		} else {
			userPay.setPuid(userId);
		}

		walletService.update(userPay);
		return Result.ok(userPay);
	}

	@ApiOperation("修改钱包获取验证码")
	@RequestMapping("/send/phone")
	public Result sendPhone() {
		User user = AccountUtils.getUser();
		String sessionId = SecurityUtils.getSubject().getSession().getId().toString();
		String code = UUIDUtil.generateNumber(6);
		String key = "edit:wallet" + emailCodeCacheService.builderKey(sessionId, code);
		phoneCodeCahceService.putCode(key, code);
		String outId = user.getUserPhone();
		try {
			smsService.sendSmsCode(user.getUserPhone(), code, outId);
		} catch (Exception e) {
		}

		return Result.ok(code);
	}

	@ApiOperation("获取钱包类型下拉框")
	@RequestMapping("/select/map")
	public Result getCurrency() {
		Map<String, Object> result = currencyService.getCurrency();
		return Result.ok(result);
	}

	@RequestMapping("/income/record")
	@ApiOperation("获得收益记录")
	public Result getIncomeRecord() {
		PageModel model = ParseRequestUtils.getObjectValue(PageModel.class);
		UserFppsRecord userFppsRecord = ParseRequestUtils.getObjectValue(UserFppsRecord.class);

		IPage<UserFppsRecord> page = new Page<>(model);
		if (userFppsRecord == null) {
			userFppsRecord = new UserFppsRecord();
		}
		User user = AccountUtils.getUser();
		Long userId = user.getUserId();
		IPage<UserFppsRecord> pages = userPayBillItemService.getIncomeRecord(page, userId);
		return Result.ok(pages);
	}

	@RequestMapping("/pay/record")
	@ApiOperation("获得支付记录")
	public Result getPayRecord() {
		PageModel model = ParseRequestUtils.getObjectValue(PageModel.class);
		UserPayBillItem wallet = ParseRequestUtils.getObjectValue(UserPayBillItem.class);

		IPage<Map<String, Object>> page = new Page<>(model);
		if (wallet == null) {
			wallet = new UserPayBillItem();
		}

//		User user = AccountSecurityUtils.getUser();
//		wallet.setPuid(user.getUserId());
		wallet.setPuid(AccountUtils.getCurrentUserId());

		IPage<Map<String, Object>> pages = userPayBillItemService.getPayRecord(page, wallet);
		return Result.ok(pages);
	}

	@RequestMapping("/pay/record/export")
	@ApiOperation("获得支付记录")
	public String getPayRecordExprot(@RequestBody User user, HttpServletResponse response) throws IOException {
		UserPayBillItem wallet = ParseRequestUtils.getObjectValue(UserPayBillItem.class);

		if (wallet == null) {
			wallet = new UserPayBillItem();
		}
		wallet.setPuid(user.getUserId());

//		String fileName = user.getUsername() + "_" + DateUtil.getYYYYMMddHHmmss(new Date());
//		response.setHeader("content-type", "application/octet-stream;charset=UTF-8");
//		response.setContentType("application/octet-stream");
//		// 下载文件能正常显示中文
//		response.setHeader("Content-Disposition",
//				"attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
//		Object[] he = { "交易TXID", "时间", "金额(BTC)" };
//		List<Object> head = Arrays.asList(he);

		List<List<Object>> dataList = userPayBillItemService.getPayRecordExport(wallet);
		return GsonUtil.getGson().toJson(dataList);
//		HSSFWorkbook hssfWorkbook = ExcelUtil.getHSSFWorkbook("default", head, dataList, null);
//		hssfWorkbook.write(response.getOutputStream());
	}

	@RequestMapping("/walletAddress/validated")
	@ApiOperation("钱包地址校验")
	public Result walletAddressValidated() throws IOException {
		String walletAddress = ParseRequestUtils.getStringByKey("walletAddress");

		boolean bitCoinAddressValidate = CoinValidationUtil.bitCoinAddressValidate(walletAddress);
		if (!bitCoinAddressValidate) {
			throw new AccountException(ExceptionCode.coin_error);
		}
		return Result.ok();
	}

}
