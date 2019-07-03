package com.mpool.admin.module.bill.controller;

import com.mpool.admin.module.bill.service.PoolRateService;
import com.mpool.admin.module.log.service.LogAdminOperationService;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.common.Result;
import com.mpool.common.model.admin.SysUser;
import com.mpool.common.model.log.LogUserOperation;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.ExecutorService;

@RestController
@RequestMapping({ "/pool/rate", "/apis/pool/rate" })
public class PoolRateController {
	@Autowired
	private PoolRateService poolRateService;
	@Autowired
	private ExecutorService executorService;
	@Autowired
	private LogAdminOperationService userOperationService;

	@GetMapping("/update")
	@ApiOperation("修改矿池费率")
	public Result updatePoolRate(@RequestParam(value = "poolRate") Float poolRate) {
		poolRateService.updatePoolRate(poolRate);
		//记录日志
		SysUser sysUser = AdminSecurityUtils.getUser();//获取当前登录的管理员
		executorService.execute(() -> {
					LogUserOperation model = new LogUserOperation();
					model.setLogType(2);//2矿池日志
					model.setUserType("admin");//操作人类型是管理员
					model.setCreatedTime(new Date());
					model.setUserId(sysUser.getUserId());
					model.setContent("管理员["+sysUser.getUsername()+"]-修改矿池费率（会员）为["+poolRate+"]，操作成功");
					userOperationService.insert(model);
		});
		return Result.ok();
	}
}
