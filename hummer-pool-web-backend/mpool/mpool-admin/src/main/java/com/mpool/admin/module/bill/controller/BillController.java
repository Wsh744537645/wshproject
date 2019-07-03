package com.mpool.admin.module.bill.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.InModel.InBlock;
import com.mpool.admin.mapperUtils.bill.UserPayBillMapperUtils;
import com.mpool.admin.module.account.model.OutUserPayBillItem;
import com.mpool.admin.module.bill.service.UserPayBillItemService;
import com.mpool.admin.module.bill.service.UserPayBillService;
import com.mpool.admin.module.bill.service.impl.UserPayBillServiceImpl;
import com.mpool.admin.module.log.service.LogAdminOperationService;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.common.Result;
import com.mpool.common.annotation.CurrentUser;
import com.mpool.common.model.account.bill.UserPayBill;
import com.mpool.common.model.account.bill.UserPayBillItem;
import com.mpool.common.model.admin.SysUser;
import com.mpool.common.model.log.LogUserOperation;
import com.mpool.common.util.BTCUtil;
import com.mpool.common.util.CSVUtil;
import com.mpool.common.util.DateUtil;
import com.mpool.common.util.ExcelUtil;
import com.mpool.common.util.page.Page;
import com.mpool.common.util.page.PageModel;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutorService;

@RestController
@RequestMapping({ "/bill", "/apis/bill" })
public class BillController {

	private static final Logger log = LoggerFactory.getLogger(UserPayBillServiceImpl.class);

	@Autowired
	private UserPayBillItemService btcUserPayBillItemService;
	@Autowired
	private UserPayBillService btcUserPayBillService;
	@Autowired
	private UserPayBillMapperUtils userPayBillMapperUtils;
	@Autowired
	private ExecutorService executorService;
	@Autowired
	private LogAdminOperationService userOperationService;

	@GetMapping("/due/items")
	@ApiOperation("获取 待生成账单")
	public Result getDuePayItems(PageModel model, String username, String day) {
		IPage<Map<String, Object>> iPage = new Page<>(model);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("username", username);
		Long time = (day == null ? null : Long.parseLong(day));
		IPage<Map<String, Object>> duePayItems = btcUserPayBillItemService.getDuePayItems(iPage, param, time);
		return Result.ok(duePayItems);
	}

	@GetMapping("/due/PayBtcSum")
	@ApiOperation("获取 待生成账单页面的余额合计")
	public Result getDuePayBtcSum(String username) {
		List<UserPayBillItem> list = btcUserPayBillItemService.getDuePayBtcSum(username);
		Long duePayBtcSum = 0L;
		for (UserPayBillItem userPayBillItem : list) {
			Long payBtc = userPayBillItem.getPayBtc();
			if (payBtc != null) {
				duePayBtcSum += payBtc;
			}
		}
		return Result.ok(duePayBtcSum);
	}

	@PutMapping("/due/updateListPayBct")
	@ApiOperation("修改待生成账单的余额")
	public Result updateListPayBct(@RequestParam("payBctRate") Double payBctRate) {

		btcUserPayBillItemService.updateListPayBct(payBctRate);
		//记录日志
		SysUser sysUser = AdminSecurityUtils.getUser();//获取当前登录的管理员
		executorService.execute(() -> {
			if (payBctRate != null){
					LogUserOperation model = new LogUserOperation();
					model.setLogType(2);//2矿池日志
					model.setUserType("admin");//操作人类型是管理员
					model.setCreatedTime(new Date());
					model.setUserId(sysUser.getUserId());
					model.setContent("管理员["+sysUser.getUsername()+"]-批量修改矿池待生成账单比例为["+payBctRate+"]，操作成功");
					userOperationService.insert(model);
			}
		});
		return Result.ok();
	}

	@PostMapping("/createBillNumber")
	@ApiOperation("创建支付单")
	public Result createBillNumber(@CurrentUser SysUser user, @RequestBody String[] itemIds) {
		Long userId = user.getUserId();
		ArrayList<Long> arrayList = new ArrayList<Long>();
		for (String id : itemIds) {
			arrayList.add(Long.parseLong(id));
		}
		btcUserPayBillService.createBillNumber(userId, arrayList);

		return Result.ok();
	}

	@PostMapping("/rollbackBillItem")
	@ApiOperation("回滚待支付账单")
	public Result rollBackBillItem(@CurrentUser SysUser user, @RequestParam("bill_number") String billNumber, @RequestBody String[] itemIds){
		ArrayList<Long> arrayList = new ArrayList<Long>();
		for (String id : itemIds) {
			arrayList.add(Long.parseLong(id));
		}

		btcUserPayBillService.rollBackBillItem(user.getUserId(), billNumber, arrayList);
		return Result.ok();
	}

	@GetMapping("/info")
	@ApiOperation("获得billinfo 待支付账单页面")
	public Result getBillInfo(PageModel model, String orderId, String txid, Date startTime, Date endTime,
			Integer status) {
		IPage<Map<String, Object>> iPage = new Page<>(model);
		Map<String, Object> param = new HashMap<>();

		if (endTime != null) {
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(endTime);
			rightNow.add(Calendar.HOUR, 23);
			rightNow.add(Calendar.MINUTE, 59);
			rightNow.add(Calendar.SECOND, 59);
			endTime = rightNow.getTime();
			// iPage = btcUserPayBillService.getBillInfo(iPage, param);
		}
		param.put("orderId", orderId);
		param.put("txid", txid);
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		param.put("status", status);

		iPage = btcUserPayBillService.getBillInfo(iPage, param);
		Long payBtcSum = userPayBillMapperUtils.getBillInfoSum(param);
		Map<String, Object> map = new HashMap<>();
		map.put("sum", payBtcSum);
		map.put("page", iPage);
		return Result.ok(map);
	}

	@GetMapping("/info/exprot")
	@ApiOperation("导出billinfo 待支付账单页面")
	public void getBillInfoExprot(PageModel model, String orderId, String txid, Date startTime, Date endTime,
			Integer status, HttpServletResponse response) throws IOException {
		IPage<Map<String, Object>> iPage = new Page<>(model);
		Map<String, Object> param = new HashMap<>();
		iPage.setSize(Long.MAX_VALUE);
		if (endTime != null) {
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(endTime);
			rightNow.add(Calendar.HOUR, 23);
			rightNow.add(Calendar.MINUTE, 59);
			rightNow.add(Calendar.SECOND, 59);
			endTime = rightNow.getTime();
			// iPage = btcUserPayBillService.getBillInfo(iPage, param);
		}
		param.put("orderId", orderId);
		param.put("txid", txid);
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		param.put("status", status);
		List<List<Object>> data = new ArrayList<>();
		iPage = btcUserPayBillService.getBillInfo(iPage, param);
		for (Map<String, Object> map : iPage.getRecords()) {
			List<Object> list = new ArrayList<>();
			list.add(map.get("bill_number"));
			list.add(map.get("txid"));
			list.add(map.get("cteate_at"));
			list.add(BTCUtil.formatBTCNONull(new BigDecimal(map.get("pay_all_btc").toString())));
			if ("1".equals(map.get("status").toString())) {
				list.add("完成");
			} else {
				list.add("未完成");
			}
			list.add(map.get("operat"));
			data.add(list);
		}
		String fileName = "BillInfo" + "_" + DateUtil.getYYYYMMddHHmmss(new Date());
		fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
		response.setHeader("content-type", "application/octet-stream;charset=UTF-8");
		response.setContentType("application/octet-stream");
		// 下载文件能正常显示中文
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
		Object[] he = { "单号", "TXID", "时间", "BTC", "状态", "操作人" };

		List<Object> head = Arrays.asList(he);
		HSSFWorkbook hssfWorkbook = ExcelUtil.getHSSFWorkbook("default", head, data, null);
		hssfWorkbook.write(response.getOutputStream());

	}

//	@GetMapping("/info/payBctSum")
//	@ApiOperation("获得billinfo 待支付账单页面btc合计")
//	public Result getBillInfoPayBctSum(String orderId, String txid, Date startTime, Date endTime, Integer status) {
//		Map<String, Object> param = new HashMap<>();
//		param.put("orderId", orderId);
//		param.put("txid", txid);
//		param.put("startTime", startTime);
//		param.put("endTime", endTime);
//		param.put("status", status);
//		// 获取btc累计和
//
//		Long payBtcSum = userPayBillMapper.getBillInfoSum(param);
//		return Result.ok(payBtcSum);
//	}

	@GetMapping("/bill/item/info/{billNumber}")
	@ApiOperation("获取bill下单item")
	public Result getBillItemInfo(@PathVariable(value = "billNumber") String billNumber) {
		List<LinkedHashMap<String, Object>> result = btcUserPayBillItemService.getBillItemInfo(billNumber);
		return Result.ok(result);
	}

	@GetMapping("exportBillItems/{billNumber}")
	@ApiOperation("导出支付钱包地址")
	public void exportBillItems(@PathVariable(value = "billNumber") String billNumber, HttpServletResponse response)
			throws IOException {
		response.setHeader("content-type", "application/octet-stream");
		response.setContentType("application/octet-stream");
		// 下载文件能正常显示中文
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(billNumber + ".csv", "UTF-8"));
		List<List<Object>> data = btcUserPayBillItemService.getCSVDataItems(billNumber);
		ServletOutputStream outputStream = response.getOutputStream();
		CSVUtil.createCSVFile(null, data, outputStream);
	}

	@GetMapping("/pay/{billNumber}")
	@ApiOperation("支付账单")
	public Result pay(@RequestParam(value = "txid") String txid,
			@PathVariable(value = "billNumber") String billNumber) {
		btcUserPayBillService.pay(billNumber, txid);
		return Result.ok();
	}

	@GetMapping("/pay/bill/list")
	@ApiOperation("获得已支付账单")
	public Result getPayBillItemList(PageModel model, Integer day, String walletAddr) {
		IPage<Map<String, Object>> page = new Page<Map<String, Object>>(model);
		Map<String, Object> param = new HashMap<>();
		param.put("day", day);
		param.put("walletAddr", walletAddr);
		page = btcUserPayBillItemService.getPayBillItemList(page, param);
		return Result.ok(page);
	}

	@GetMapping("/pay/bill/list/export")
	@ApiOperation("导出已支付账单")
	public void getPayBillItemListExport(PageModel model, Integer day, String walletAddr, HttpServletResponse response)
			throws IOException {
		IPage<Map<String, Object>> page = new Page<Map<String, Object>>(model);
		page.setSize(Integer.MAX_VALUE);
		Map<String, Object> param = new HashMap<>();
		param.put("day", day);
		param.put("walletAddr", walletAddr);
		page = btcUserPayBillItemService.getPayBillItemList(page, param);

		String fileName = "BillPayInfoList" + "_" + DateUtil.getYYYYMMddHHmmss(new Date());
		fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
		List<List<Object>> data = new ArrayList<>();

		for (Map<String, Object> map : page.getRecords()) {
			List<Object> list = new ArrayList<>();
			list.add(map.get("cteateAt"));
			list.add(map.get("username"));
			if (map.get("payBtc") != null) {

				list.add(BTCUtil.formatBTCNONull(Long.parseLong(map.get("payBtc").toString())));
			} else {
				list.add(0);
			}
			list.add(map.get("walletAddress"));
			list.add(map.get("mUsername"));
			list.add(map.get("operat"));
			list.add(map.get("payAt"));
			data.add(list);
		}
		response.setHeader("content-type", "application/octet-stream;charset=UTF-8");
		response.setContentType("application/octet-stream");
		// 下载文件能正常显示中文
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
		Object[] he = { "账单创建时间", "打币账户", "BTC金额", "打币地址", "主账号名", "操作人", "支付时间" };

		List<Object> head = Arrays.asList(he);
		HSSFWorkbook hssfWorkbook = ExcelUtil.getHSSFWorkbook("default", head, data, null);
		hssfWorkbook.write(response.getOutputStream());
	}

	@PostMapping("/getBillInfoByNumOrTxid")
	@ApiOperation("根据单号或txid搜索")
	public Result getBillInfoByNumOrTxid(@RequestBody UserPayBill userPayBill) {
		String bilNum = userPayBill.getBillNumber();
		String txid = userPayBill.getTxid();
		UserPayBill userPayBill1Info = btcUserPayBillService.getBillInfoByNumOrTxid(bilNum, txid);
		return Result.ok(userPayBill1Info);
	}

	@PostMapping("/getBillInfoByDate")
	@ApiOperation("通过时间段搜索获得billinfo")
	public Result getBillInfoByDate(@RequestBody InBlock date, PageModel model) {
		IPage<Map<String, Object>> iPage = new Page<>(model);
		Map map = btcUserPayBillService.getBillInfoByDate(date, iPage);
		return Result.ok(map);
	}

	@GetMapping("exportBillPayInfoList")
	@ApiOperation("导出BillPay信息（单号,TXID, 时间,BTC,状态,操作人）")
	public void exportBillPayInfoList(InBlock date, HttpServletResponse response) throws IOException {
		String fileName = "BillPayInfoList" + "_" + DateUtil.getYYYYMMddHHmmss(new Date());
		fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");

		List<List<Object>> data = btcUserPayBillService.exportBillInfo(date);
		response.setHeader("content-type", "application/octet-stream;charset=UTF-8");
		response.setContentType("application/octet-stream");
		// 下载文件能正常显示中文
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
		Object[] he = { "单号", "TXID", "时间", "BTC", "状态", "操作人" };

		List<Object> head = Arrays.asList(he);
//		log.debug("head => {}", head);
//		log.debug("date => {}", data);
		HSSFWorkbook hssfWorkbook = ExcelUtil.getHSSFWorkbook("default", head, data, null);
		hssfWorkbook.write(response.getOutputStream());
	}

	@GetMapping("/due/itemByUsername")
	@ApiOperation("通过用户名查询待支付的Item")
	public Result getDuePayitemByUsername(String username, PageModel model) {
		IPage<UserPayBillItem> iPage = new Page<>(model);
		IPage<UserPayBillItem> duePayItems = btcUserPayBillItemService.getDuePayItemList(iPage, username);
		return Result.ok(duePayItems);
	}

	@GetMapping("exportDuePayItems")
	@ApiOperation("导出待生成账单")
	public void exportDuePayItems(String username, HttpServletResponse response) throws IOException {
		String fileName = "DuePayItems" + "_" + DateUtil.getYYYYMMddHHmmss(new Date());
		fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");

		List<List<Object>> data = btcUserPayBillItemService.exportDuePayItems(username);
		response.setHeader("content-type", "application/octet-stream;charset=UTF-8");
		response.setContentType("application/octet-stream");
		// 下载文件能正常显示中文
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
		Object[] he = { "用户名", "钱包地址", "余额（BTC）", "创建时间" };

		List<Object> head = Arrays.asList(he);
//		log.debug("head => {}", head);
//		log.debug("date => {}", data);
		HSSFWorkbook hssfWorkbook = ExcelUtil.getHSSFWorkbook("default", head, data, null);
		hssfWorkbook.write(response.getOutputStream());
	}

	@PostMapping("/addPayBillInfo")
	@ApiOperation("创建支付单(待生成账单页面-新增)")
	public Result addPayBillInfo(@RequestBody OutUserPayBillItem outUserPayBillItem) {

		BigDecimal paybtc = BigDecimal.valueOf(outUserPayBillItem.getPayBtc()).multiply(BigDecimal.valueOf(100000000));
		log.debug("request data -> {} paybtc-> {} paybtclong  -> {}", outUserPayBillItem, paybtc.toPlainString(),
				paybtc.longValue());
		UserPayBillItem userPayBillItem = new UserPayBillItem();
		userPayBillItem.setPayBtc(paybtc.longValue());
		userPayBillItem.setUsername(outUserPayBillItem.getUsername());
		userPayBillItem.setDesc(outUserPayBillItem.getDesc());
		btcUserPayBillItemService.addPayBillInfo(userPayBillItem);
		//记录日志
		SysUser sysUser = AdminSecurityUtils.getUser();//获取当前登录的管理员
		executorService.execute(() -> {
				LogUserOperation model = new LogUserOperation();
				model.setLogType(2);//2矿池日志
				model.setUserType("admin");//操作人类型是管理员
				model.setCreatedTime(new Date());
				model.setUserId(sysUser.getUserId());
				model.setContent("管理员["+sysUser.getUsername()+"]-新增矿池待生成账单,用户名["+outUserPayBillItem.getUsername()+"],金额["+outUserPayBillItem.getPayBtc()+"],类型["+outUserPayBillItem.getDesc()+"],操作成功");
				userOperationService.insert(model);
		});
		return Result.ok();
	}
}
