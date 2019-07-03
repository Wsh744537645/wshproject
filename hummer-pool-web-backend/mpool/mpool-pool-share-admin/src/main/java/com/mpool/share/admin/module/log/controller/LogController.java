package com.mpool.share.admin.module.log.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.share.admin.InModel.OutUserLog;
import com.mpool.share.admin.module.bill.service.UserBillService;
import com.mpool.share.admin.module.log.service.UserRegLogService;
import com.mpool.common.Result;
import com.mpool.common.model.account.log.UserRegLog;
import com.mpool.common.util.page.Page;
import com.mpool.common.util.page.PageModel;
import com.mpool.share.admin.module.product.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping({ "/log", "/apis/log" })
public class LogController {
	@Autowired
	private UserRegLogService userRegLogService;
	@Autowired
	private UserBillService billService;
	@Autowired
	private ProductService productService;

	@GetMapping("/getAccountRegisterLog")
	@ApiOperation("获得account用户注册日志")
	public Result getAccountRegisterLog(PageModel model, UserRegLog userRegLog) {
		model.setAscs(null);
		model.setDescs(null);
		Page<UserRegLog> page = new Page<>(model);
		IPage<Map<String, Object>> iPage = userRegLogService.getAccountRegisterLog(page, userRegLog);
		return Result.ok(iPage);
	}

	@GetMapping("/getUserLog")
	@ApiOperation("获得用户日志")
	public Result getUserLog(PageModel model,String usernaem) {
		IPage<OutUserLog> page = new Page<>(model);
		IPage<OutUserLog> billPayLog = userRegLogService.getUserLog(page,usernaem);
		return Result.ok(billPayLog);
	}

	@GetMapping("/getPayUserLog")
	@ApiOperation("获得打款日志")
	public Result getPayUserLog(PageModel model){
		IPage<Map<String, Object>> page = new Page<>(model);
		page = billService.getAdminPayLog(page);
		return Result.ok(page);
	}

	@GetMapping("/getProductLog")
	@ApiOperation("获得商品日志")
	public Result getProductLog(PageModel model){
		IPage<Map<String, Object>> page = new Page<>(model);
		page = productService.getProductLogList(page);
		return Result.ok(page);
	}

}
