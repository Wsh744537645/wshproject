package com.mpool.admin.module.account.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.InModel.InBlock;
import com.mpool.admin.module.account.service.FppsRatioDayService;
import com.mpool.admin.module.log.service.LogAdminOperationService;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.common.Result;
import com.mpool.common.model.account.fpps.FppsRatioDay;
import com.mpool.common.model.admin.SysUser;
import com.mpool.common.model.log.LogUserOperation;
import com.mpool.common.util.DateUtil;
import com.mpool.common.util.ExcelUtil;
import com.mpool.common.util.page.Page;
import com.mpool.common.util.page.PageModel;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutorService;

@RestController
@RequestMapping(value = { "/fee", "/apis/fee" })
public class MinerFeeController {
	@Autowired
	private FppsRatioDayService fppsRatioDayService;
	@Autowired
	private ExecutorService executorService;
	@Autowired
	private LogAdminOperationService userOperationService;

	/**
	 * 获得矿工费列表
	 * 
	 * @param model
	 * @param inblock
	 * @return
	 */
	@GetMapping("miner/rate")
	@ApiOperation("获取每一天的费率")
	public Result getMinerFeeRateList(PageModel model, InBlock inblock) {
		IPage<FppsRatioDay> iPage = new Page<>(model);
		String yyyymMdd = DateUtil.getYYYYMMdd(new Date());
		iPage = fppsRatioDayService.getMinerFeeRateList(iPage, inblock);
		Map<String, Object> map = new HashMap<>();
		map.put("page", iPage);
		map.put("day", yyyymMdd);
		return Result.ok(map);
	}

	@GetMapping("miner/rate/export")
	@ApiOperation("导出每一天的费率明细（按时间段搜索）")
	public void exportHistoryFppsRateDays(InBlock date, PageModel model, HttpServletResponse response)
			throws IOException {
		IPage<FppsRatioDay> page = new com.mpool.common.util.page.Page<>(model);
		page.setSize(Integer.MAX_VALUE);
		String fileName = "FppsRateDays" + "_" + DateUtil.getYYYYMMddHHmmss(new Date());
		fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");

		List<List<Object>> data = fppsRatioDayService.exportHistoryFppsRateDays(date, page);
		response.setHeader("content-type", "application/octet-stream;charset=UTF-8");
		response.setContentType("application/octet-stream");
		// 下载文件能正常显示中文
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
		Object[] he = { "日期", "旧费率", "新费率" };

		List<Object> head = Arrays.asList(he);
		HSSFWorkbook hssfWorkbook = ExcelUtil.getHSSFWorkbook("default", head, data, null);
		hssfWorkbook.write(response.getOutputStream());
	}

	/**
	 * 修改矿工费
	 * 
	 * @param
	 * @param
	 * @return
	 */
	@PutMapping("miner/rate")
	public Result updateMinerFeeRate(Float rate) {
		fppsRatioDayService.updateFppsRateDay(rate);
		//记录日志
		SysUser sysUser = AdminSecurityUtils.getUser();//获取当前登录的管理员
		executorService.execute(() -> {
			LogUserOperation model = new LogUserOperation();
			model.setLogType(2);//2矿池日志
			model.setUserType("admin");//操作人类型是管理员
			model.setCreatedTime(new Date());
			model.setUserId(sysUser.getUserId());
			model.setContent("管理员["+sysUser.getUsername()+"]-修改矿工费为["+rate+"],操作成功	");
			userOperationService.insert(model);
		});
		return Result.ok();
	}

}
