package com.mpool.share.admin.module.system.controller;

import com.mpool.share.admin.config.shiro.token.UsernamePasswordTelphoneToken;
import com.mpool.share.admin.exception.AdminException;
import com.mpool.share.admin.exception.ExceptionCode;
import com.mpool.share.admin.module.log.service.LogAdminOperationService;
import com.mpool.share.admin.module.system.mapper.SysUserMapper;
import com.mpool.share.admin.module.system.model.AuthModel;
import com.mpool.share.admin.module.system.service.AuthService;
import com.mpool.share.admin.utils.AdminSecurityUtils;
import com.mpool.common.Result;
import com.mpool.common.model.admin.SysUser;
import com.mpool.common.model.log.LogUserOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
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

	@PostMapping("/login")
	@ApiOperation("登录接口")
	public Result login(@RequestBody AuthModel authModel) {
		String adminname = authModel.getUsername();
		SysUser admin = sysUserMapper.findByUsername(adminname);
		if (admin == null) {
			throw new AdminException(ExceptionCode.admin_exists);
		}

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
	}

	@GetMapping("logout")
	@ApiOperation("登出接口")
	public Result logout() {
		authService.logout();
		return Result.ok();
	}
}
