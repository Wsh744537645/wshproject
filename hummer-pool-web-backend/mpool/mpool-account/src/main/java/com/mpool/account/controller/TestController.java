package com.mpool.account.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import com.mpool.account.service.fpps.FppsRatioDayService;
import com.mpool.account.service.fpps.SettlementService;
//import com.mpool.tasks.task.FppsTask;
//import com.mpool.tasks.task.NotifyTask;
import com.mpool.account.utils.RequestResolveUtil;
import com.mpool.common.Result;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/test")
public class TestController {
	@Autowired
	FppsRatioDayService fppsRatioDayService;
	//@Autowired
	//private FppsTask fppsTask;
	@Autowired
	private TemplateEngine templateEngine;
	//@Autowired
	//private NotifyTask notifyTask;

	@Autowired
	private SettlementService settlementService;

	@GetMapping("/date")
	@ApiOperation("时间测试")
	public Date date(@RequestParam("date") Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(dateFormat.format(date));
		return date;
	}

	@GetMapping("/fpps/test")
	public Result testFppsRatioDayService() {
//		fppsTask.taskFppsRatio();
//		fppsTask.taskUserFppsRate();
		//fppsTask.taskSettlement();
		//fppsTask.taskBill();
		return Result.ok();
	}

	@GetMapping(value = "/fpps/template", produces = "text/html")
	public String testTemplateEngine() {
		Map<String, Object> va = new HashMap<>();
		va.put("captcha", "123456");
		va.put("title", "注册验证码");
		va.put("type", "hummerpool");
		IContext context = new Context(RequestResolveUtil.getRequestLocale(), va);
		String process = templateEngine.process("email/email.html", context);
		return process;
	}

	@PostMapping("/recommed")
	@ApiOperation("推荐人奖励")
	public Result recommed(@RequestParam(value = "day") Integer day) {
		settlementService.settlement(day);
		return Result.ok();
	}

//	@Autowired
//	ScheduledTasks scheduledTasks;
//
//	@GetMapping("/schedu")
//	@ApiOperation("推荐人奖励11")
//	public Result result(@RequestParam(value = "day") String day) {
//		scheduledTasks.getBtcComBlock(day);
//		return Result.ok();
//	}

}
