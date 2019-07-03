package com.mpool.admin.module.dashboard.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.InModel.InBlock;
import com.mpool.admin.InModel.InBlockchain;
import com.mpool.admin.module.account.mapper.FoundBlocksMapper;
import com.mpool.admin.module.account.service.UserService;
import com.mpool.admin.module.dashboard.service.PoolApiService;
import com.mpool.admin.module.dashboard.service.impl.DashboardServiceImpl;
import com.mpool.admin.utils.AccountSecurityUtils;
import com.mpool.common.model.account.User;
import com.mpool.common.model.pool.StatsUsersDay;
import com.mpool.common.util.CSVUtil;
import com.mpool.common.util.DateUtil;
import com.mpool.common.util.ExcelUtil;
import com.mpool.common.util.page.Page;
import com.mpool.common.util.page.PageModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mpool.admin.module.dashboard.service.DashboardService;
import com.mpool.common.Result;
import com.mpool.common.model.account.FoundBlocks;
import com.mpool.common.model.pool.StatsPoolDay;
import com.mpool.common.model.pool.StatsPoolHour;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping(value = { "/dashboard", "/apis/dashboard" })
public class DashboardController {

	private static final Logger log = LoggerFactory.getLogger(DashboardServiceImpl.class);

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private PoolApiService poolApiService;

	@Autowired
	private UserService userService;

	@GetMapping("/currentPoolInfo")
	@ApiOperation("当前pool算力与矿工活跃数")
	public Result currentPoolInfo() {
		Map<String, Object> result = dashboardService.currentPoolInfo();
		return Result.ok(result);
	}

	@GetMapping("/getHistoryBlock")
	@ApiOperation("获取全部爆块记录数")
	public Result getHistoryBlock() {
		List<FoundBlocks> data = dashboardService.getHistoryBlock();
		return Result.ok(data);
	}

	@GetMapping("/past30DayShare")
	@ApiOperation("过去30 天池算力变化")
	public Result getPoolPast30DayShare() {
		//List<StatsPoolDay> list = dashboardService.getPoolPast30DayShare();
		List<StatsPoolDay> list = dashboardService.getPoolPast30DayShareCache();
		return Result.ok(list);
	}

	@GetMapping("/past30DayWorker")
	@ApiOperation("过去30 天池矿机活跃数")
	public Result getPoolPast30DayWorker() {
		//List<Map<String, Object>> list = dashboardService.getPoolPast30DayWorker();
		//return Result.ok(list);
		return Result.ok(dashboardService.getPoolPast30DayWorkerCache());
	}

	@GetMapping("/past24HShare")
	@ApiOperation("过去24 小时池算力变化")
	public Result getPoolPast24HShare() {
		List<StatsPoolHour> list = dashboardService.getPoolPast24HShare();
		return Result.ok(list);
	}

	@PostMapping("/getHistoryBlockList")
	@ApiOperation("获取爆块记录列表(时间搜索，带分页)")
	@ResponseBody
	public Result getHistoryBlockList(@RequestBody InBlock date, PageModel model) {
		IPage<FoundBlocks> page = new Page<>(model);
		IPage<FoundBlocks> iPage = dashboardService.getHistoryBlockList(date, page);
		return Result.ok(iPage);
	}

	@PostMapping("/getHistoryBlockRewardsSum")
	@ApiOperation("获取爆块奖励总和(时间搜索)")
	@ResponseBody
	public Result getHistoryBlockRewardsSum(@RequestBody InBlock date) {
		IPage<FoundBlocks> page = new Page<>();
		page.setSize(Integer.MAX_VALUE);
		// 获取所有爆块奖励和
		IPage<FoundBlocks> iPage = dashboardService.getHistoryBlockList(date, page);
		List<FoundBlocks> list = iPage.getRecords();
		Map<String, Object> map = new HashMap<>();
		Long rewardsSum = 0L;
		for (FoundBlocks blocks : list) {
			Long rewards = blocks.getRewards();
			rewardsSum += rewards;
		}
		BigDecimal f = BigDecimal.valueOf(rewardsSum).divide(BigDecimal.valueOf(100000000), 8, BigDecimal.ROUND_DOWN);
		map.put("rewardsSum", f);
		return Result.ok(map);
	}

	@GetMapping("exportHistoryBlockList")
	@ApiOperation("导出爆块记录（按时间段搜索）")
	public void exportBillItems(InBlock date, HttpServletResponse response) throws IOException {
		String fileName = "historyBlockList" + "_" + DateUtil.getYYYYMMddHHmmss(new Date());
		fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");

		List<List<Object>> data = dashboardService.exportHistoryBlockList(date);
		response.setHeader("content-type", "application/octet-stream;charset=UTF-8");
		response.setContentType("application/octet-stream");
		// 下载文件能正常显示中文
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
		Object[] he = { "块高度", "块时间", "hash", "确认个数", "奖励", "体积" };

		List<Object> head = Arrays.asList(he);
		log.debug("head => {}", head);
		log.debug("date => {}", data);
		HSSFWorkbook hssfWorkbook = ExcelUtil.getHSSFWorkbook("default", head, data, null);
		hssfWorkbook.write(response.getOutputStream());
	}

	@GetMapping("/getBlockchainInfo")
	@ApiOperation("过去每天全网算力和全网爆块数")
	public Result getBlockchainInfo(InBlock date, PageModel model) {
		// 设置size 默认为20
		if (model.getSize() == 10) {
			model.setSize(20);
		}
		log.debug("start Time =>{},end Time => {}", date.getStrTime(), date.getEndTime());
		IPage<InBlockchain> page = new Page<>(model);
		if (date.getStrTime() != null) {
			date.setStrTime(DateUtil.addDay(date.getStrTime(), 1));	//当前时间加1天
		}
		if (date.getEndTime() != null) {
			date.setEndTime(DateUtil.addDay(date.getEndTime(), 1));
		}
		IPage<InBlockchain> list = dashboardService.getBlockchainInfo(page, date);
		return Result.ok(list);
	}

	@GetMapping("/getBlockchainInfo/export")
	@ApiOperation("过去30全网算力和全网爆块数")
	public void getHistoryBlockListExport(InBlock date, PageModel model, HttpServletResponse response)
			throws IOException {
		IPage<InBlockchain> page = new Page<>(model);
		page.setSize(Integer.MAX_VALUE);
		IPage<InBlockchain> iPage = dashboardService.getBlockchainInfo(page, date);
		List<List<Object>> data = new ArrayList<>();
		for (InBlockchain inBlockchain : iPage.getRecords()) {
			List<Object> list = new ArrayList<>();
			list.add(inBlockchain.getCreatedDay());
			list.add(inBlockchain.getCurrentHashRate());
			list.add(inBlockchain.getHashRate());
			list.add(inBlockchain.getBlocks());
			list.add(inBlockchain.getRate());
			data.add(list);
		}
		response.setHeader("content-type", "application/octet-stream;charset=UTF-8");
		response.setContentType("application/octet-stream");
		// 下载文件能正常显示中文
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(DateUtil.getYYYYMMddHHmmss(new Date()) + ".xls", "UTF-8"));
		Object[] he = { "时间", "24小时算力", "全网算力", "全网爆块", "全网算力占比" };

		List<Object> head = Arrays.asList(he);
		log.debug("head => {}", head);
		log.debug("date => {}", data);
		HSSFWorkbook hssfWorkbook = ExcelUtil.getHSSFWorkbook("default", head, data, null);
		hssfWorkbook.write(response.getOutputStream());

	}

	@GetMapping("/getUser24H")
	@ApiOperation("获取用户24小时 算力(点击子账号弹出)")
//	@RequiresPermissions("son")//权限设置
	public Result getUser24H(Long userId) {

		// 获取当前登录用户
//		User user = AccountSecurityUtils.getUser();
//		Long userId = user.getUserId();
		List<Map<String, Object>> hour24 = null;
		hour24 = poolApiService.getUser24H(userId);
		return Result.ok(hour24);
	}

	@GetMapping("/getUser30Share")
	@ApiOperation("获取用户30天算力")
	public Result getUser30ShareInfo(Long userId){
		log.info("getuser30_share, id={}", userId);
		List<StatsUsersDay> usersDayList = userService.getUser30DayShare(userId);
		return Result.ok(usersDayList);
	}

	@GetMapping("/getUser24HWorkers")
	@ApiOperation("获取用户24小时矿机活跃数")
	public Result getUser24HWorkersInfo(Long userId){
		log.info("getuser24_worker, id={}", userId);
		List<Map<String, Object>> usersHourList = userService.getUser24HWorker(userId);
		return Result.ok(usersHourList);
	}

	@GetMapping("/getWorker24H")
	@ApiOperation("获取矿机24小时的算力")
	public Result getWorker24HInfo(@RequestParam("workerId") String workerId){
		Long id = Long.parseLong(workerId);
		List<Map<String, Object>> workerHourList = poolApiService.getWorker24HByWorkerId(id);
		return Result.ok(workerHourList);
	}

}
