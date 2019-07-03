package com.mpool.admin.module.system.controller;

import com.mpool.admin.config.shiro.token.UsernamePasswordTelphoneToken;
import com.mpool.admin.exception.AdminException;
import com.mpool.admin.exception.ExceptionCode;
import com.mpool.admin.module.log.service.LogAdminOperationService;
import com.mpool.admin.module.system.mapper.SysUserMapper;
import com.mpool.admin.module.system.model.AuthModel;
import com.mpool.admin.module.system.service.AuthService;
import com.mpool.admin.module.system.service.CurrencyService;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.common.Result;
import com.mpool.common.model.account.Currency;
import com.mpool.common.model.admin.SysUser;
import com.mpool.common.model.log.LogUserOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@RestController
@RequestMapping(value = { "/auth", "apis/auth" })
@Api("认证接口")
public class AuthController {
	@Autowired
	private AuthService authService;
	@Autowired
	private ExecutorService executorService;
	@Autowired
	private LogAdminOperationService userOperationService;
	@Autowired
	private SysUserMapper sysUserMapper;
	@Autowired
	private CurrencyService currencyService;

	@PostMapping("/login")
	@ApiOperation("登录接口")
	public Result login(@RequestBody AuthModel authModel) {
		String adminname = authModel.getUsername();
		SysUser admin = sysUserMapper.findByUsername(adminname);
		if (admin == null) {
			throw new AdminException(ExceptionCode.admin_exists);
		}
//		判断该admin是否是正常人员0已离职1在职
		Integer status = 1;//admin.getStatus();
		if (status != null && !status.equals(0)){
			UsernamePasswordTelphoneToken token = new UsernamePasswordTelphoneToken(adminname,
					authModel.getPassword(), "0");
			SysUser emp = authService.login(token);
			//记录日志
			executorService.execute(() -> {
				SysUser sysUser = sysUserMapper.findByUsername(authModel.getUsername());
				if (sysUser != null){
					Long userId = sysUser.getUserId();
					if (userId != null){
						LogUserOperation model = new LogUserOperation();
						model.setLogType(3);//3是系统日志
						model.setUserType("admin");//操作人类型是管理员
						model.setCreatedTime(new Date());
						model.setUserId(userId);
						model.setContent("管理员["+sysUser.getUsername()+"]-登录成功");
						userOperationService.insert(model);
					}
				}
			});
			return Result.ok(emp);
		}else {
			throw new AdminException(ExceptionCode.admin_close);
		}
	}

	@GetMapping("logout")
	@ApiOperation("登出接口")
	public Result logout() {
		authService.logout();
		return Result.ok();
	}

	@RequestMapping("/switch/currency")
	@ApiOperation("切换币种")
	public Result switchCurrency(String currencyName){
		if(currencyName == null){
			currencyName = "BTC";
		}
		AdminSecurityUtils.setCurrencyName(currencyName);
		Map<String, String> map = new HashMap<>(1);
		map.put("currencyName", currencyName);
		return Result.ok(map);
	}

	@GetMapping("/getCurrencyList")
	@ApiOperation("获得当前支持的币种列表")
	public Result getCurrencyList(){
		List<Currency> currencies = currencyService.selectListByEnable();
		return Result.ok(currencies);
	}

	@GetMapping("/cur/currencyName")
	@ApiOperation("获得当前切换的币种")
	public Result getCurCurrencyName(){
		String currencyName = AdminSecurityUtils.getCurrencyName();
		Map<String, String> map = new HashMap<>(1);
		map.put("currencyName", currencyName);
		return Result.ok(map);
	}
}
