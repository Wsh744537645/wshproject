package com.mpool.account.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.account.constant.Constant;
import com.mpool.account.constant.Constant.ReceiveCode;
import com.mpool.account.exception.AccountException;
import com.mpool.account.exception.ExceptionCode;
import com.mpool.account.mapper.UserMapper;
import com.mpool.account.model.SubAccountModel;
import com.mpool.account.model.UserInfo;
import com.mpool.account.model.UserPayModel;
import com.mpool.account.model.WalletModel;
import com.mpool.account.model.subaccount.SubAccountList;
import com.mpool.account.service.*;
import com.mpool.account.service.bill.UserPayService;
import com.mpool.account.service.log.UserOperationService;
import com.mpool.account.utils.AccountSecurityUtils;
import com.mpool.common.Result;
import com.mpool.common.cache.aspect.annotation.CheckSecurityCode;
import com.mpool.common.model.account.User;
import com.mpool.common.model.account.UserOpenResource;
import com.mpool.common.model.account.UserRegion;
import com.mpool.common.model.account.bill.UserPay;
import com.mpool.common.model.log.LogUserOperation;
import com.mpool.common.util.DateUtil;
import com.mpool.common.util.SpringUtil;
import com.mpool.common.util.page.Page;
import com.mpool.common.util.page.PageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;

@RequestMapping({ "/userManager", "/apis/userManager" })
@RestController
@Api("用户管理")
public class UserManageController {

	private static final Logger log = LoggerFactory.getLogger(UserManageController.class);

	@Autowired
	private UserService usersService;
	@Autowired
	private UserOpenResourceService userOpenResourceService;
	@Autowired
	private CurrencyMiniPayService currencyMiniPayService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private UserWalletPayTypeService userWalletPayTypeService;
	@Autowired
	private ExecutorService executorService;
	@Autowired
	private UserOperationService userOperationService;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserPayService userPayService;

	@PostMapping("/createSubAccount")
	@ApiOperation("创建子账号")
	@RequiresPermissions("master")
	@Transactional
	public Result createSubaccount(@Valid @RequestBody SubAccountModel subaccount, HttpServletRequest request) {
		// 获取用户当前语言环境
		Locale locale = RequestContextUtils.getLocale(request);
		// 获取当前登录用户
		User currUser = AccountSecurityUtils.getUser();
		// user
		User users = new User();
		users.setUsername(subaccount.getUsername());
		users.setPassword(subaccount.getPassword());
		users.setLangType(locale.toString());
		users.setCreateBy(currUser.getUserId());
		users.setMasterUserId(currUser.getUserId());
		// 区域
		UserRegion userRegion = new UserRegion();
		userRegion.setRegionId(subaccount.getRegionId());
		// 钱包
		WalletModel[] wallets = subaccount.getWallets();
		if (!(wallets != null && wallets.length == 1)) {
			throw new AccountException("wallet.info.error", "钱包信息错误!");
		}
		WalletModel model = wallets[0];

		Integer currencyId = currencyService.selectListByCurrencyName(model.getWalletType());

		String walletAddress = model.getWalletAddr();

		Integer payModel = userWalletPayTypeService.payIdByType(subaccount.getPayType());

		currencyMiniPayService.checkMinPay(currencyId, model.getMiniPay());

		Long minPay = model.getMiniPay().multiply(BigDecimal.valueOf(Math.pow(10, 8))).longValue();
		UserPay userPay = new UserPay();

		userPay.setCurrencyId(currencyId);

		userPay.setPayModel(payModel);

		userPay.setMinPay(minPay);

		userPay.setWalletAddress(walletAddress);

		usersService.createSubAccount(users, userPay, userRegion);
		return Result.ok();
	}

	@PostMapping("/createPay")
	@ApiOperation("新建钱包")
	public Result createPay(@Valid @RequestBody UserPayModel userPayModel){
		User user = usersService.findByUsername(userPayModel.getUsername());
		if(user == null){
			throw new AccountException(ExceptionCode.account_not_exists);
		}

		userPayService.addWalletInfo(user.getUserId(), userPayModel);

		return Result.ok();
	}

	@GetMapping("/findSubAccount")
	@ApiOperation("查看子账号(子账号清单)")
//	@RequiresPermissions("master")
	public Result findSubAccount(PageModel pageModel, User user, @RequestParam("currencyName") String currencyName) {
		// 获取当前登录用户
		User currUser = AccountSecurityUtils.getUser();
		if (user == null) {
			user = new User();
		}
		IPage<SubAccountList> page = new Page<>(pageModel);
		user.setMasterUserId(currUser.getUserId());
		IPage<SubAccountList> pageResult = usersService.findSubAccountInfo(page, user, currencyName);
		return Result.ok(pageResult);
	}

	@GetMapping("/findUserInfo")
	@ApiOperation("查看获得子账号详情")
	public Result findUserInfo() {
		// 获取当前登录用户
		User currUser = AccountSecurityUtils.getUser();
		User user = new User();
		user.setUserId(currUser.getUserId());
		IPage<SubAccountList> pageResult = usersService.findSubAccountInfo(new Page<>(), user, AccountSecurityUtils.getCurPayCurrencyName());
		return Result.ok(pageResult.getRecords().get(0));
	}

	@GetMapping("/switchSubAccount/{userId}")
	@ApiOperation("切换子账号")
	// @RequiresPermissions("userManager:switch:subAccount")
	public Result switchSubAccount(@PathVariable(value = "userId") Long userId, String currencyName) {
		AccountSecurityUtils.getSubject().getSession().setAttribute("currencyName", currencyName);
		User user = usersService.switchSubAccount(userId);
		return Result.ok(user);
	}

	@ApiOperation("获得当前登录用户")
	@GetMapping("/current/getUserInfo")
	public Result getUserInfo() {
		User user = usersService.getCurrentUser();
		return Result.ok(user);
	}

//	@ApiOperation("获得当前子账号")
//	@GetMapping("/current/getSubAccount")
//	@RequiresPermissions("")
//	public Result getSubAccount() {
//		Object user = SecurityUtils.getSubject().getSession().getAttribute(C_SUB_USER_KEY);
//		return Result.ok(user);
//	}

	@ApiOperation("修改密码（用户端用户管理页面）")
	@PostMapping("/current/change/password")
	public Result changePassword(String oldPassword, String newPassword) {
		User user = AccountSecurityUtils.getUser();
		oldPassword = new String(Base64.getDecoder().decode(oldPassword));
		newPassword = new String(Base64.getDecoder().decode(newPassword));
		log.debug("oldPassword => {}, newPassword=> {}", oldPassword, newPassword);
		usersService.updatePassword(user.getUsername(), oldPassword, newPassword);

		//记录日志
		executorService.execute(() -> {
			if (user != null){
					LogUserOperation model = new LogUserOperation();
					model.setLogType(1);
					model.setUserType("account");
					model.setCreatedTime(new Date());
					model.setUserId(user.getUserId());
					model.setContent("用户["+user.getUsername()+"]-修改密码操作成功");
					userOperationService.insert(model);
				}

		});
		return Result.ok();
	}

	@ApiOperation("修改子账户密码")
	@PostMapping("/master/change/subUserPassword")
	@RequiresPermissions("master")
	public Result masterChangeSubUserPassword(@RequestBody String newPassword, @RequestParam("userId") Long userId) {

		usersService.updateSubUserPassword(userId, newPassword);

		return Result.ok();
	}

	@ApiOperation("修改邮箱")
	@PostMapping("/current/change/email")
	public Result changeEmail(String email) {
		User user = AccountSecurityUtils.getUser();
		Object attribute = AccountSecurityUtils.getSubject().getSession().getAttribute(Constant.securityCode);
		if (attribute == null) {
			throw AccountException.security();
		} else {
			AccountSecurityUtils.getSubject().getSession().removeAttribute(Constant.securityCode);
		}
		usersService.updateEmail(user.getUsername(), email);
		return Result.ok();
	}

	@ApiOperation("修改手机")
	@PostMapping("/current/change/phone")
	public Result changePhone(String phone) {
		User user = AccountSecurityUtils.getUser();
		Object attribute = AccountSecurityUtils.getSubject().getSession().getAttribute(Constant.securityCode);
		if (attribute == null) {
			throw AccountException.security();
		} else {
			AccountSecurityUtils.getSubject().getSession().removeAttribute(Constant.securityCode);
		}
		usersService.updatePhone(user.getUsername(), phone);
		return Result.ok();
	}

	@ApiOperation("发送手机短信")
	@GetMapping("/send/phone")
	public Result chchePhone(
			@Validated @RequestParam("phone") @Pattern(regexp = "^1[1-9]\\d{9}$", message = "请输入正确手机号码") String phone) {
		String code = usersService.sendCodeByPhone(phone);
		if (SpringUtil.getActiveProfile() == "prod") {
			code = null;
		}
		return Result.ok(code);
	}

	@ApiOperation("修改当前用户信息")
	@PostMapping("/current/change/info")
	public Result changePhone(@RequestBody UserInfo userInfo) {
		User user = AccountSecurityUtils.getUser();
		userInfo.setUsername(user.getUsername());
		usersService.updateUserInfo(userInfo);
		return Result.ok();
	}

	@ApiOperation("发送邮箱验证码")
	@GetMapping("/send/email")
	public Result chcheEmail(@Validated @RequestParam("email") @Email String email) {
		String code = usersService.sendSecurityCodeEmail(email);
		if (SpringUtil.getActiveProfile() == "prod") {
			code = null;
		}
		return Result.ok(code);
	}

	@PostMapping("/share")
	@ApiOperation("子账号管理的分享页面")
	public Result share(@RequestParam(required = false) Integer d, @RequestParam(required = false) Integer m,
			@RequestParam(required = false) Integer h, @RequestParam(required = true) Long userId,
						@RequestParam(required = true) String currencyName, HttpServletRequest request) {
		String resid;
		Date createTime = new Date();
		Date expiryTime = null;
		int i = 0;
		//过期时间设置200天2/13修改
		 d = 200;
		if (d != null && d > 0) {
			expiryTime = DateUtil.addDay(createTime, d);
			i++;
		}
		if (m != null && m > 0) {
			expiryTime = DateUtil.addMinute(createTime, m);
			i++;
		}
		if (h != null && h > 0) {
			expiryTime = DateUtil.addHour(createTime, h);
			i++;
		}
//		2/13日需求变更取消过期时间
//		if (i != 1) {
//			throw new AccountException("12333421", "过期时间只能选填一个");
//		}
		String host = request.getRequestURL().toString().replace(request.getRequestURI(), "");
		UserOpenResource entity = new UserOpenResource();
//		取消过期时间
		entity.setExpiryTime(expiryTime);
		entity.setCreateTime(createTime);
		entity.setUserId(userId);
		entity.setCurrencyName(currencyName);
		resid = userOpenResourceService.insertRet(entity);
		return Result.ok(host + "/share/" + resid);
	}

	@GetMapping("/getSecurityCode")
	public Result getSecurityCode(ReceiveCode receiveCode) {

		User currentUser = usersService.getCurrentUser();
		String code;
		switch (receiveCode) {
		case email:
			code = usersService.sendSecurityCodeEmail(currentUser.getUserEmail());
			break;
		case phone:
			code = usersService.sendSecurityCodePhone(currentUser.getUserPhone());
			break;
		default:
			throw AccountException.paramEx();
		}
		if (SpringUtil.getActiveProfile() == "prod") {
			code = null;
		}
		return Result.ok(code);
	}

	@GetMapping("/checkSecurityCode")
	@CheckSecurityCode
	public Result checkSecurityCode() {
		Session session = AccountSecurityUtils.getSubject().getSession();
		session.setAttribute(Constant.securityCode, "");
		return Result.ok();
	}

}
