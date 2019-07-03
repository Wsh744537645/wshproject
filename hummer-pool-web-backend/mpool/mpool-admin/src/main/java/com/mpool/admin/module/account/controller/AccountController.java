package com.mpool.admin.module.account.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.module.account.mapper.UserMapper;
import com.mpool.admin.module.account.model.MasterUserShaerModel;
import com.mpool.admin.module.account.model.UserPayBillOut;
import com.mpool.admin.module.account.service.UserService;
import com.mpool.admin.module.bill.service.UserPayService;
import com.mpool.admin.module.dashboard.service.impl.DashboardServiceImpl;
import com.mpool.admin.module.log.service.LogAdminOperationService;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.common.Result;
import com.mpool.common.model.account.User;
import com.mpool.common.model.admin.SysUser;
import com.mpool.common.model.log.LogUserOperation;
import com.mpool.common.model.pool.StatsUsersDay;
import com.mpool.common.model.pool.StatsWorkersDay;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutorService;

@RestController
@RequestMapping(value = { "/account", "/apis/account" })
@Api("account 站用户管理")
public class AccountController {

	private static final Logger log = LoggerFactory.getLogger(DashboardServiceImpl.class);

	@Autowired
	private UserService userService;
	@Autowired
	private UserPayService btcUserPayService;
	@Autowired
	private ExecutorService executorService;
	@Autowired
	private LogAdminOperationService userOperationService;
	@Autowired
	private UserMapper userMapper;


	@GetMapping("list/page")
	@ApiOperation("分页查询用户")
	public Result listAccount(PageModel model, User user) {
		IPage<User> page = new Page<User>(model);
		IPage<Map<String, Object>> iPage = userService.listPage(page, user);
		return Result.ok(iPage);
	}

	@GetMapping("master/list/page")
	@ApiOperation("分页主账号用户")
	public Result masterListAccount(PageModel model, User user) {
		IPage<User> page = new Page<User>(model);
		IPage<UserPayBillOut> iPage = userService.masterListPage(page, user);
		return Result.ok(iPage);
	}

	@GetMapping("member/list/page")
	@ApiOperation("获得主账号下面的 子账号")
	public Result memberListAccount(PageModel model, User user) {
		IPage<User> page = new Page<User>(model);
		IPage<UserPayBillOut> iPage = userService.memberListPage(page, user);
		return Result.ok(iPage);
	}

	@GetMapping("member/list/export")
	@ApiOperation("导出获得主账号下面的 子账号")
	public void memberListAccountExport(PageModel model, User user, HttpServletResponse response) throws IOException {
		IPage<User> page = new Page<User>(model);
		page.setSize(Integer.MAX_VALUE);
		String fileName = user.getUsername() + "_" + DateUtil.getYYYYMMddHHmmss(new Date());
		IPage<UserPayBillOut> iPage = userService.memberListPage(page, user);
		List<List<Object>> dataList = new ArrayList<>();
		iPage.getRecords().forEach(re -> {
			List<Object> list = new ArrayList<>();
			list.add(re.getUsername());
			list.add(re.getCurrentShare());
			list.add(re.getPastDayShare());
			list.add(re.getYesterday() / 100000000d);
			list.add(re.getTotalDue() / 100000000d);
			list.add(re.getTotalRevenue() / 100000000d);
			list.add(re.getTotalPaid() / 100000000d);
			list.add(re.getShare24T());
			list.add(re.getShare30T());
			dataList.add(list);
		});

		response.setHeader("content-type", "application/octet-stream;charset=UTF-8");
		response.setContentType("application/octet-stream");
		// 下载文件能正常显示中文
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));

		Object[] he = { "子账户名", "当前总算力", "过去一天总算力", "昨日收益", "余额(BTC)", "总收益", "累计转账", "24小时算力", "30天算力" };
		List<Object> head = Arrays.asList(he);
		HSSFWorkbook hssfWorkbook = ExcelUtil.getHSSFWorkbook("default", head, dataList, null);
		hssfWorkbook.write(response.getOutputStream());
	}

	@GetMapping("master/list")
	@ApiOperation("运维-主账号用户")
	public Result masterListAccountList(PageModel model, User user) {
		IPage<User> page = new Page<User>(model);
		IPage<UserPayBillOut> iPage = userService.masterListPage(page, user);
		iPage.getRecords().forEach(m -> {
			m.setFppsRate(0);
		});
		return Result.ok(iPage);
	}

//	@GetMapping("member/list")
//	@ApiOperation("运维-主账号下面的 子账号")
//	public Result memberListAccountList(PageModel model, User user) {
//		IPage<User> page = new Page<User>(model);
//		IPage<UserPayBillOut> iPage = userService.memberListPage(page, user);
//		iPage.getRecords().forEach(m -> {
//			m.remove("fppsRate");
//		});
//		return Result.ok(iPage);
//	}

	@PutMapping("/updateFppsRate")
	@ApiOperation("修改费率用户")
	public Result updateFppsRate(@RequestParam("userId") Long userId, @RequestParam("fppsRate") Float fppsRate) {

		btcUserPayService.updateFppsRate(userId, fppsRate);
		//记录日志
		SysUser sysUser = AdminSecurityUtils.getUser();//获取当前登录的管理员
		executorService.execute(() -> {
			if (userId != null){
				User userInfo = userMapper.getUserInfoByUserId(userId);
				if (userInfo != null){
					String username = userInfo.getUsername();
				LogUserOperation model = new LogUserOperation();
				model.setLogType(2);//2是矿池日志
				model.setUserType("admin");//操作人类型是管理员
				model.setCreatedTime(new Date());
				model.setUserId(sysUser.getUserId());
				model.setContent("管理员["+sysUser.getUsername()+"]-修改用户["+username+"]费率为["+fppsRate+"]，操作成功");
				userOperationService.insert(model);
			}
			}
		});
		return Result.ok();
	}

	@GetMapping("/master/getUser30Day")
	@ApiOperation("获取用户30天 算力")
	public Result getUser30Day(@RequestParam(name = "userId") Long userId) {
		// 获取当前登录用户
		List<MasterUserShaerModel> masterUser30DayShare = userService.getMasterUser30DayShare(userId);
		return Result.ok(masterUser30DayShare);
	}

//	@GetMapping("master/getUserListByUsername")
//	@ApiOperation("根据用户名查询主账号（当前算力,账号余额，昨日收益，总收益，累计转账...")
//	public Result getUserListByusername(String username) {
//		List<UserPayBillOut> iPage = userService.getUserListByUsername(username);
//		return Result.ok(iPage);
//	}

	@GetMapping("/exportMasterBillItems")
	@ApiOperation("导出主账号列表（可按用户名搜索）")
	public void exportMasterBillItems(User user, HttpServletResponse response) throws IOException {
		String fileName = "masterUserBillList" + "_" + DateUtil.getYYYYMMddHHmmss(new Date());
		fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");

		List<List<Object>> data = userService.exportMasterList(user);
		response.setHeader("content-type", "application/octet-stream;charset=UTF-8");
		response.setContentType("application/octet-stream");
		// 下载文件能正常显示中文
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
		Object[] he = { "主账号名", "当前总算力（15分钟）", "过去一天总算力", "昨日收益", "余额BTC", "总收益", "累计转账"};

		List<Object> head = Arrays.asList(he);
		log.debug("head => {}", head);
		log.debug("date => {}", data);
		HSSFWorkbook hssfWorkbook = ExcelUtil.getHSSFWorkbook("default", head, data, null);
		hssfWorkbook.write(response.getOutputStream());
//		CSVUtil.createCSVFile(head, data, response.getOutputStream());

	}

	@GetMapping("user/fpps/list")
	@ApiOperation("挖矿费率设置 挖矿费率页面列表")
	public Result getUserFppsList(PageModel model, Integer group, String username, String recommendName) {

		IPage<Map<String, Object>> iPage = new Page<>(model);
		Map<String, Object> map = new HashMap<>();
		iPage = btcUserPayService.getUserFppsList(iPage, group, username, recommendName);
		map.put("page", iPage);
		return Result.ok(map);
	}

	@GetMapping("user/fpps/list/export")
	@ApiOperation("导出 挖矿费率页面列表")
	public void getUserFppsListExport(PageModel model, Integer group, String username, String recommendName,
			HttpServletResponse response) throws IOException {
		IPage<Map<String, Object>> iPage = new Page<>(model);
		iPage.setSize(Integer.MAX_VALUE);
		iPage = btcUserPayService.getUserFppsList(iPage, group, username, recommendName);
		List<List<Object>> data = new ArrayList<>();
		for (Map<String, Object> map : iPage.getRecords()) {
			List<Object> list = new ArrayList<>();
			list.add(map.get("username"));
			list.add(map.get("fpps_rate"));
			list.add(map.get("type"));
			if (map.get("fpps_group") != null) {
				if ("1".equals(map.get("fpps_group").toString())) {
					list.add("基石");
				} else {
					list.add("会员");
				}
			} else {
				list.add("");
			}

			list.add(map.get("recommend_username"));
			list.add(map.get("fee_rate"));
			data.add(list);
//			<td>{{items.}}</td>
//           	<td>{{items.}}</td>
//           	<td>{{items.}}</td>
//           	<td>{{items. == 1 ? '基石' : '会员' }}</td>
//           	<td>{{items.}}</td>
//           	<td>{{items.fee_rate}}</td>
		}
		String fileName = DateUtil.getYYYYMMddHHmmss(new Date());
		fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
		response.setHeader("content-type", "application/octet-stream;charset=UTF-8");
		response.setContentType("application/octet-stream");
		// 下载文件能正常显示中文
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
		Object[] he = { "用户名", "费率", "挖矿模式", "类型", "推荐人", "推荐费率" };

		List<Object> head = Arrays.asList(he);
		log.debug("head => {}", head);
		log.debug("date => {}", data);
		HSSFWorkbook hssfWorkbook = ExcelUtil.getHSSFWorkbook("default", head, data, null);
		hssfWorkbook.write(response.getOutputStream());
	}

//	@PutMapping("/updateFppsRateDay")
//	@ApiOperation("修改费率昨天")
//	public Result updateFppsRateDay(@RequestParam("fppsRate") Float fppsRate) {
//		fppsRatioDayService.updateFppsRateDay(fppsRate);
//		return Result.ok();
//	}

//	@GetMapping("/getFppsRateDays")
//	@ApiOperation("获取每一天的费率")
//	public Result getFppsRateDays(InBlock date, PageModel model) {
//		IPage<FppsRatioDay> page = new Page<>(model);
//		IPage<FppsRatioDay> pageList = fppsRatioDayService.getFppsRateDays(date, page);
//		return Result.ok(pageList);
//	}

	@PostMapping("/getMasterUserInfoList")
	@ApiOperation("获取主账号信息（主/子账号，注册时间，最后登录时间，注册手机，邮箱，用户类型）")
	public Result getMasterUserInfoList(@RequestBody User user, PageModel model) {
		IPage<User> page = new Page<>(model);
		String username = user.getUsername();
		IPage<User> pageList = userService.getUserInfos(page, username);
		return Result.ok(pageList);
	}

	@PutMapping("/updatePasswordByUsername")
	@ApiOperation("帮用户重置密码")
	public Result updatePasswordByUsername(@RequestParam("username") String username) {

		userService.updatePasswordByUsername(username);
		//记录日志
		executorService.execute(() -> {
			if (username != null){
				User userInfo = userMapper.getUserInfo(username);
				if (userInfo != null){
					LogUserOperation model = new LogUserOperation();
					model.setLogType(3);//3系统日志
					model.setUserType("admin");//操作人类型是管理员
					model.setCreatedTime(new Date());
					SysUser sysUser = AdminSecurityUtils.getUser();//获取当前登录的管理员
					model.setUserId(sysUser.getUserId());
					model.setContent("管理员["+sysUser.getUsername()+"]-帮用户["+username+"]重置密码，操作成功");
					userOperationService.insert(model);
				}
			}
		});
		return Result.ok();
	}

	@GetMapping("exportMasterUserInfoList")
	@ApiOperation("导出主/子账号，注册时间，最后登录时间，注册手机，邮箱，用户类型）")
	public void exportMasterUserInfoList(User user, PageModel model, HttpServletResponse response) throws IOException {
		IPage<User> page = new Page<>(model);
		String fileName = "UserInfoList" + "_" + DateUtil.getYYYYMMddHHmmss(new Date());
		fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");

		List<List<Object>> data = userService.exportMasterUserInfoList(user.getUsername());
		response.setHeader("content-type", "application/octet-stream;charset=UTF-8");
		response.setContentType("application/octet-stream");
		// 下载文件能正常显示中文
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
		Object[] he = { "主/子用户名", "注册时间", "最后登录时间", "注册手机", "注册邮箱", "账户类型" };

		List<Object> head = Arrays.asList(he);
		log.debug("head => {}", head);
		log.debug("date => {}", data);
		HSSFWorkbook hssfWorkbook = ExcelUtil.getHSSFWorkbook("default", head, data, null);
		hssfWorkbook.write(response.getOutputStream());
	}

	@GetMapping("/check/username")
	@ApiOperation("检测用户名是否有效")
	public Result checkUsername(@RequestParam(value = "username", required = true) String username) {
		boolean boole = userService.getCheckUsername(username);
		return Result.ok(boole);
	}



//	public static void main(String[] args){
//
//		String ip ="183.14.29.215";
//		RestTemplate restTemplate = new RestTemplate();
//		ResponseEntity<String> forEntity = restTemplate
//				.getForEntity("http://ip.taobao.com/service/getIpInfo.php?ip={ip}", String.class,ip);
////				.getForEntity("http://ip-api.com/docs/api:json", String.class,ip);
//		Gson gson = new Gson();
//		JsonObject fromJson = gson.fromJson(forEntity.getBody(), JsonObject.class);
//
//		JsonElement regionName = fromJson.getAsJsonObject("data").get("region");
//		JsonElement cityName = fromJson.getAsJsonObject("data").get("city");
//
//		String region = regionName.toString();
//		String city = cityName.toString();
////		String name = region+"-"+city;
//		System.out.println("登录城市："+region+city);
//		System.out.println(fromJson);
//	}

	@GetMapping("/ip/address")
	@ApiOperation("通过ip获取城市信息")
	public Result ipAddress() {
		String ip ="183.14.29.215";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> forEntity = restTemplate
				.getForEntity("http://freeapi.ipip.net/{ip}", String.class,ip);
		String fromJson =forEntity.getBody();
//		JSONObject obj = new JSONObject(fromJson);
//		String regionName = (String) obj.getJSONObject("data").get("regionName");
//		String cityName = (String) obj.getJSONObject("data").get("city");
//		String name = regionName+"-"+cityName;
		return Result.ok(fromJson);
	}
}
