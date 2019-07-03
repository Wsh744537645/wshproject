package com.mpool.account.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutorService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mpool.account.mapper.UserMapper;
import com.mpool.account.service.*;
import com.mpool.account.service.log.UserOperationService;
import com.mpool.common.model.account.UserOpenResource;
import com.mpool.common.model.log.LogUserOperation;
import com.mpool.common.properties.MultipleProperties;
import com.mpool.common.util.*;
import com.mpool.rpc.RpcInstance;
import com.mpool.rpc.account.producer.btc.MultipleShare;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.account.exception.AccountException;
import com.mpool.account.exception.ExceptionCode;
import com.mpool.account.model.WalletModel;
import com.mpool.account.service.bill.UserPayBillItemService;
import com.mpool.account.service.bill.UserPayService;
import com.mpool.account.utils.AccountSecurityUtils;
import com.mpool.common.CoinValidationUtil;
import com.mpool.common.Result;
import com.mpool.common.cache.aspect.annotation.CheckCodeEmail;
import com.mpool.common.cache.aspect.annotation.CheckCodeSms;
import com.mpool.common.cache.service.EmailCodeCacheService;
import com.mpool.common.cache.service.PhoneCodeCahceService;
import com.mpool.common.model.account.User;
import com.mpool.common.model.account.bill.UserPay;
import com.mpool.common.model.account.bill.UserPayBillItem;
import com.mpool.common.model.account.fpps.UserFppsRecord;
import com.mpool.common.util.page.Page;
import com.mpool.common.util.page.PageModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
@RequestMapping({ "/wallet", "/apis/wallet" })
@Api("钱包管理")
@Slf4j
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
	@Autowired
	private MultipleProperties multipleProperties;
	@Autowired
	private RpcInstance rpcInstance;
	@Autowired
	private UserOpenResourceService userOpenResourceService;

	@ApiOperation("修改钱包")
	@PostMapping("/user/wallet/edit")
	@CheckCodeEmail(prefix = "edit:wallet")
	@ResponseBody
	public Result editUserWallet(@RequestBody WalletModel walletModel,
			@RequestParam(required = false, value = "userId") Long userId) {
		UserPay userPay = new UserPay();

		userPay.setId((walletModel.getWalletId()));

		BigDecimal minPay = walletModel.getMiniPay();

		String walletType = walletModel.getWalletType();

		Integer currencyId = currencyService.selectListByCurrencyName(walletType);

		currencyMiniPayService.checkMinPay(currencyId, minPay.divide(BigDecimal.valueOf(Math.pow(10, 8))));

		//userPay.setMinPay(walletModel.getMiniPay().multiply(BigDecimal.valueOf(Math.pow(10, 8))).longValue());

		userPay.setWalletAddress(walletModel.getWalletAddr());

		userPay.setCurrencyId(currencyId);

		if (userId == null) {
			User user = AccountSecurityUtils.getUser();
			userPay.setPuid(user.getUserId());
		} else {
			userPay.setPuid(userId);
		}

		walletService.update(userPay);

		return Result.ok(userPay);
	}

	@ApiOperation("获取登录用户的钱包")
	@GetMapping("/user/wallet/info")
	@ResponseBody
	public Result getUserWallet(Integer currencyId) {
		if(currencyId == null){
			//默认btc
			currencyId = new Integer(1);
		}
		User user = userService.getCurrentUser();
		WalletModel walletModel = walletService.getWalletInfo(user.getUserId(), currencyId);
		walletModel.setEmail(user.getUserEmail());
		walletModel.setPhone(user.getUserPhone());
		return Result.ok(walletModel);
	}

	@ApiOperation("修改钱包获取验证码")
	@GetMapping("/send/email")
	@ResponseBody
	public Result sendEmail() {
		User user = userService.getCurrentUser();
		String sessionId = SecurityUtils.getSubject().getSession().getId().toString();

		String code = UUIDUtil.generateNumber(6);
		String key = "edit:wallet" + emailCodeCacheService.builderKey(sessionId, code);
		emailCodeCacheService.putCode(key, code);
		emailService.sendSimple(user.getUserEmail(), "修改钱包验证码", code);
		return Result.ok(code);
	}

	@ApiOperation("修改钱包地址")
	@PostMapping("/user/wallet/edit/phone")
	@CheckCodeSms(prefix = "edit:wallet")
	@ResponseBody
	public Result editUserWalletPhone(@RequestBody WalletModel walletModel,
			@RequestParam(required = false, value = "userId") Long userId) {
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
			User user = AccountSecurityUtils.getUser();
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
	@PostMapping("/user/wallet/edit/minPay")
	@ResponseBody
	public Result editUserWalletMinPay(@RequestParam("minPay") BigDecimal minPay,
			@RequestParam("walletType") String walletType,
			@RequestParam(required = false, value = "userId") Long userId) {
		UserPay userPay = new UserPay();

		Integer currencyId = currencyService.selectListByCurrencyName(walletType);

		currencyMiniPayService.checkMinPay(currencyId, minPay);

		userPay.setMinPay(minPay.multiply(BigDecimal.valueOf(Math.pow(10, 8))).longValue());

		userPay.setCurrencyId(currencyId);

		if (userId == null) {
			User user = AccountSecurityUtils.getUser();
			userPay.setPuid(user.getUserId());
		} else {
			userPay.setPuid(userId);
		}

		walletService.update(userPay);
		return Result.ok(userPay);
	}

	@ApiOperation("修改钱包获取验证码")
	@GetMapping("/send/phone")
	@ResponseBody
	public Result sendPhone() {
		User user = userService.getCurrentUser();
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
	@GetMapping("/select/map")
	@ResponseBody
	public Result getCurrency() {
		Map<String, Object> result = currencyService.getCurrency();
		return Result.ok(result);
	}

	@GetMapping("/income/record")
	@ApiOperation("获得收益记录")
	public String getIncomeRecord(PageModel model, UserFppsRecord userFppsRecord){
		String currencyName = AccountSecurityUtils.getCurPayCurrencyName();
		if(currencyName == null || currencyName.equals(multipleProperties.getName())) {
			return "forward:/wallet/income/recordbtc";
		}
		else{

			return "forward:/mutiple/" + currencyName + "?url=wallet/income/record";
		}
	}

	@GetMapping("/income/recordbtc")
	@ApiOperation("获得收益记录")
	@ResponseBody
	public Result getIncomeRecordBtc(PageModel model, UserFppsRecord userFppsRecord) {
		IPage<UserFppsRecord> page = new Page<>(model);
		if (userFppsRecord == null) {
			userFppsRecord = new UserFppsRecord();
		}
		User user = AccountSecurityUtils.getUser();
		Long userId = user.getUserId();
		IPage<UserFppsRecord> pages = userPayBillItemService.getIncomeRecord(page, userId);
		return Result.ok(pages);
	}

	@GetMapping("/pay/record")
	@ApiOperation("获得支付记录")
	public String getPayRecord(PageModel model, UserPayBillItem wallet){
		String currencyName = AccountSecurityUtils.getCurPayCurrencyName();
		if(currencyName == null || currencyName.equals(multipleProperties.getName())) {
			return "forward:/wallet/pay/recordbtc";
		}
		else{

			return "forward:/mutiple/" + currencyName + "?url=wallet/pay/record";
		}
	}

	@GetMapping("/pay/recordbtc")
	@ApiOperation("获得支付记录")
	@ResponseBody
	public Result getPayRecordBtc(PageModel model, UserPayBillItem wallet) {
		IPage<Map<String, Object>> page = new Page<>(model);
		if (wallet == null) {
			wallet = new UserPayBillItem();
		}

//		User user = AccountSecurityUtils.getUser();
//		wallet.setPuid(user.getUserId());
		wallet.setPuid(AccountSecurityUtils.getCurrentUserId());

		IPage<Map<String, Object>> pages = userPayBillItemService.getPayRecord(page, wallet);
		return Result.ok(pages);
	}

	@GetMapping("/pay/record/export")
	@ApiOperation("获得支付记录")
	@ResponseBody
	public void getPayRecordExprot(HttpServletResponse response, UserPayBillItem wallet, String id) throws IOException {
		if (wallet == null) {
			wallet = new UserPayBillItem();
		}
		User user;
		if(id == null) {
			user = AccountSecurityUtils.getUser();
		}else{
			UserOpenResource userOpenResource = userOpenResourceService.findById(id);
			user = userService.findById(userOpenResource.getUserId());
		}
		wallet.setPuid(user.getUserId());

		String fileName = user.getUsername() + "_" + DateUtil.getYYYYMMddHHmmss(new Date());
		response.setHeader("content-type", "application/octet-stream;charset=UTF-8");
		response.setContentType("application/octet-stream");
		// 下载文件能正常显示中文
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
		Object[] he = { "交易TXID", "时间", "金额(BTC)" };
		List<Object> head = Arrays.asList(he);

		List<List<Object>> dataList = new ArrayList<>();
		String currencyName = AccountSecurityUtils.getCurPayCurrencyName();
		if(currencyName == null || currencyName.equals(multipleProperties.getName())) {
			dataList = userPayBillItemService.getPayRecordExport(wallet);
		}
		else{
			MultipleShare multipleShare = rpcInstance.getRpcInstanceByCurrencyType(currencyName);
			if(multipleShare != null){
				String result = multipleShare.getPayRecordExprot(user);
				if(result != null){
					dataList = GsonUtil.getGson().fromJson(result, List.class);
				}else {
					log.error(">>>>多币服务[{}]被熔断，导出支付记录失败！", currencyName);
				}
			}else{
				log.error(">>>>多币服务[{}]不存在，导出支付记录失败！", currencyName);
			}
		}

		HSSFWorkbook hssfWorkbook = ExcelUtil.getHSSFWorkbook("default", head, dataList, null);
		hssfWorkbook.write(response.getOutputStream());
		// CSVUtil.createCSVFile(head, dataList, response.getOutputStream());
	}

	@GetMapping("/walletAddress/validated")
	@ApiOperation("钱包地址校验")
	@ResponseBody
	public Result walletAddressValidated(@RequestParam("walletAddress") String walletAddress) throws IOException {
		boolean bitCoinAddressValidate = CoinValidationUtil.bitCoinAddressValidate(walletAddress);
		if (!bitCoinAddressValidate) {
			throw new AccountException(ExceptionCode.coin_error);
		}
		return Result.ok();
	}

}
