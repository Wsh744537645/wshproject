package com.mpool.admin.module.trade.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.zxing.WriterException;
import com.mpool.admin.module.trade.service.TradeService;
import com.mpool.common.Result;
import com.mpool.common.util.QRCodeUtil;
import com.mpool.common.util.page.Page;
import com.mpool.common.util.page.PageModel;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/trade")
public class TradeController {

	private TradeService tradeService;

	/**
	 * 获取要支付的列表
	 * 第一列（时间：年/月/日），第二列（日算力），第三列（子账户），第四列（奖励BTC）,第五列（总付款BTC）,第六列（付款地址）。可以导出表格数据
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/getToBePay")
	@ApiOperation("获取待支付的列表")
	public Result getToBePayList(PageModel model) {
		IPage<Map<String, Object>> iPage = new Page<>(model);
		iPage = tradeService.getToBePayList(iPage);
		return Result.ok(iPage);
	}

	@GetMapping("/caseQRCode/{ctx}")
	@ApiOperation("转换二维码")
	public void caseQRCode(@PathVariable(value = "ctx") String ctx,
			@RequestParam(value = "size", required = false) Integer size, HttpServletResponse response)
			throws WriterException, IOException {
		if (size == null) {
			size = 100;
		}
		if (size > 1000) {
			size = 100;
		}
		QRCodeUtil.createQRCode(ctx, size, response.getOutputStream());
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", -1);
		response.setContentType("image/jpeg");
		response.setStatus(200);
	}
}
