package com.mpool.admin.module.log.controller;

import java.util.Map;

import com.mpool.admin.InModel.OutUserLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.module.log.service.BillPayLogService;
import com.mpool.admin.module.log.service.FppsRatelogService;
import com.mpool.admin.module.log.service.UserRegLogService;
import com.mpool.common.Result;
import com.mpool.common.model.account.log.FppsRateLog;
import com.mpool.common.model.account.log.UserRegLog;
import com.mpool.common.util.page.Page;
import com.mpool.common.util.page.PageModel;

import io.swagger.annotations.ApiOperation;

/**
 * @author chenjian2
 *
 */
@RestController
@RequestMapping({ "/log", "/apis/log" })
public class LogController {
	@Autowired
	private UserRegLogService userRegLogService;
	@Autowired
	private FppsRatelogService fppsRatelogService;
	@Autowired
	private BillPayLogService billPayLogService;


	@GetMapping("/getAccountRegisterLog")
	@ApiOperation("获得account用户注册日志")
	public Result getAccountRegisterLog(PageModel model, UserRegLog userRegLog) {
		model.setAscs(null);
		model.setDescs(null);
		Page<UserRegLog> page = new Page<>(model);
		IPage<Map<String, Object>> iPage = userRegLogService.getAccountRegisterLog(page, userRegLog);
		return Result.ok(iPage);
	}

	@GetMapping("/getFppsRateChangeLog")
	@ApiOperation("获得Fpps修改日志")
	public Result getFppsRateChangeLog(PageModel model, FppsRateLog fppsRateLog) {
		model.setAscs(null);
		model.setDescs(null);
		IPage<FppsRateLog> page = new Page<>(model);
		IPage<Map<String, Object>> iPage = fppsRatelogService.getFppsRateChangeLog(page, fppsRateLog);
		return Result.ok(iPage);
	}

	@GetMapping("/getBillPayLog")
	@ApiOperation("获得支付日志")
	public Result getBillPayLog(PageModel model) {
		model.setAscs(null);
		model.setDescs(null);
		IPage<Map<String, Object>> page = new Page<>(model);
		IPage<Map<String, Object>> billPayLog = billPayLogService.getBillPayLog(page);
		return Result.ok(billPayLog);
	}

	@GetMapping("/getUserLog")
	@ApiOperation("获得用户日志")
	public Result getUserLog(PageModel model,String usernaem) {
		IPage<OutUserLog> page = new Page<>(model);
		IPage<OutUserLog> billPayLog = userRegLogService.getUserLog(page,usernaem);
		return Result.ok(billPayLog);
	}

}
