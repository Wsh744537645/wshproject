package com.mpool.admin.module.recommend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.InModel.InBlock;
import com.mpool.admin.InModel.OutUserFppsRatio;
import com.mpool.admin.module.dashboard.service.impl.DashboardServiceImpl;
import com.mpool.admin.module.recommend.service.RecommendUserService;
import com.mpool.common.Result;
import com.mpool.common.model.account.RecommendUser;
import com.mpool.common.model.account.UserFeeRecord;
import com.mpool.common.util.DateUtil;
import com.mpool.common.util.ExcelUtil;
import com.mpool.common.util.page.Page;
import com.mpool.common.util.page.PageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

@RestController
@RequestMapping(value = { "/recommend", "/apis/recommend" })
@Api("account 站用户管理")
public class RecommendController {

	private static final Logger log = LoggerFactory.getLogger(DashboardServiceImpl.class);

	@Autowired
	private RecommendUserService recommendUserService;

	@PostMapping("creatRecommendUser")
	@ApiOperation("创建推荐人")
	public Result creatRecommendUser(@RequestBody RecommendUser recommendUser) {
		recommendUserService.creatRecommendUser(recommendUser);
		return Result.ok();
	}

	@DeleteMapping("deleteRecommendUser")
	@ApiOperation("删除推荐人")
	public Result deleteRecommendUser(@RequestParam("id") Integer id) {
		recommendUserService.deleteRecommendUser(id);
		return Result.ok();
	}

	@PostMapping("/getFppsRateAndUserTypeList")
	@ApiOperation("获取会员类型，子账号名，费率，挖矿模式FPPS(usreGroup 0会员1基石)")
	public Result getFppsRateAndUserTypeList(@RequestBody OutUserFppsRatio in, PageModel model) {
		// 设置size 默认为20
//		if (model.getSize() == 10) {
//			model.setSize(20);
//		}
		IPage<OutUserFppsRatio> page = new Page<>(model);
		IPage<OutUserFppsRatio> list = recommendUserService.getFppsRateAndUserTypeList(page, in);
		return Result.ok(list);
	}

	@GetMapping("exportHistoryFppsRateAndUserTypeList")
	@ApiOperation("导出历史子账号清单（费率，会员类型，挖矿模式）")
	public void exportHistoryFppsRateAndUserTypeList(OutUserFppsRatio in, PageModel model, HttpServletResponse response)
			throws IOException {

		IPage<OutUserFppsRatio> page = new Page<>(model);
		page.setSize(Long.MAX_VALUE);
		String fileName = "HistoryFppsRateAndUserTypeList" + "_" + DateUtil.getYYYYMMddHHmmss(new Date());
		fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");

		List<List<Object>> data = recommendUserService.exportHistoryFppsRateAndUserTypeList(page, in);
		response.setHeader("content-type", "application/octet-stream;charset=UTF-8");
		response.setContentType("application/octet-stream");
		// 下载文件能正常显示中文
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
		Object[] he = { "子账号名", "会员类型", "费率", "挖矿模式" };

		List<Object> head = Arrays.asList(he);
		log.debug("head => {}", head);
		log.debug("date => {}", data);
		HSSFWorkbook hssfWorkbook = ExcelUtil.getHSSFWorkbook("default", head, data, null);
		hssfWorkbook.write(response.getOutputStream());
	}

	@RequestMapping("/getHistoryApartCoin")//生产用
//	@GetMapping("/getHistoryApartCoin")//测试用的
	@ApiOperation("获取历史分币记录，按时间段搜索，分页（含当日分币总数 和当日奖励总数）")
	public Result getHistoryApartCoin(InBlock date, PageModel model) {
		// 设置size 默认为20
		if (model.getSize() == 10) {
			model.setSize(20);
		}
		if (date == null) {
			date = new InBlock();
		}
		log.debug("start Time =>{},end Time => {}", date.getStrTime(), date.getEndTime());
		IPage<UserFeeRecord> page = new Page<>(model);
		Map<String, Object> historyApartCoin = recommendUserService.getHistoryApartCoin(page, date);
		return Result.ok(historyApartCoin);
	}

	@GetMapping("exportHistoryApartCoinList")
	@ApiOperation("导出历史历史分币记录，按时间段搜索")
	public void exportHistoryApartCoinList(InBlock date, PageModel model, HttpServletResponse response)
			throws IOException {

		IPage<UserFeeRecord> page = new Page<>(model);
		page.setSize(Integer.MAX_VALUE);
		String fileName = "HistoryApartCoinList" + "_" + DateUtil.getYYYYMMddHHmmss(new Date());
		fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");

		List<List<Object>> data = recommendUserService.exportHistoryApartCoinList(page, date);
		response.setHeader("content-type", "application/octet-stream;charset=UTF-8");
		response.setContentType("application/octet-stream");
		// 下载文件能正常显示中文
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
		Object[] he = { "日期", "当日总分币数", "当日奖励分币数" };

		List<Object> head = Arrays.asList(he);
		log.debug("head => {}", head);
		log.debug("date => {}", data);
		HSSFWorkbook hssfWorkbook = ExcelUtil.getHSSFWorkbook("default", head, data, null);
		hssfWorkbook.write(response.getOutputStream());
	}

	@GetMapping("/getCurrentCoin")
	@ApiOperation("获得今日分币数")
	public Result getCurrentCoin() {
//		String day = DateUtil.getYYYYMMddHHmmss(new Date());
		Date day = new Date();
		Long s = recommendUserService.getCurrentCoin(day);
		Map<String, Long> historyApartCoin = new HashMap<>();

		historyApartCoin.put("day", s);
		return Result.ok(historyApartCoin);
	}

}
