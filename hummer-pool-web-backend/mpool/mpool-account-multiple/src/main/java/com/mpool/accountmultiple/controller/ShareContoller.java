package com.mpool.accountmultiple.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpool.accountmultiple.utils.AccountUtils;
import com.mpool.accountmultiple.utils.ParseRequestUtils;
import com.mpool.common.model.account.WorkerGroup;
import com.mpool.common.properties.MultipleProperties;
import com.mpool.common.util.DateUtil;
import com.mpool.common.util.GsonUtil;
import com.mpool.mpoolaccountmultiplecommon.constant.Constant;
import com.mpool.mpoolaccountmultiplecommon.model.WorkerGroupParamModel;
import com.mpool.mpoolaccountmultiplecommon.model.dashboard.UserShareChart;
import com.mpool.mpoolaccountmultiplecommon.model.dashboard.UserStatus;
import com.mpool.accountmultiple.service.PoolService;
import com.mpool.accountmultiple.service.UserOpenResourceService;
import com.mpool.accountmultiple.service.UserService;
import com.mpool.accountmultiple.service.WorkerService;
import com.mpool.accountmultiple.service.bill.UserPayBillItemService;
import com.mpool.common.Result;
import com.mpool.common.model.account.User;
import com.mpool.common.model.account.UserOpenResource;
import com.mpool.common.model.account.Worker;
import com.mpool.common.model.account.bill.UserPayBillItem;
import com.mpool.common.model.pool.StatsUsersDay;
import com.mpool.common.util.page.Page;
import com.mpool.common.util.page.PageModel;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping({ "/share", "/apis/share" })
public class ShareContoller {
	private final static Logger log= LoggerFactory.getLogger(ShareContoller.class);
	@Autowired
	private PoolService poolService;
	@Autowired
	private UserOpenResourceService userOpenResourceService;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private TemplateEngine templateEngine;
	@Autowired
	private WorkerService workerService;
	@Autowired
	private UserPayBillItemService userPayBillItemService;
	@Autowired
	private MultipleProperties multipleProperties;

	@RequestMapping("/{id}")
	@ResponseBody
	public String info(@PathVariable(value = "id") String id)
		throws JsonProcessingException {

		//request.setAttribute("key", id);
		UserOpenResource userOpenResource = userOpenResourceService.findById(id);
		Date date = new Date();
		String result = "";
		if (userOpenResource != null && userOpenResource.getExpiryTime() != null
				&& date.getTime() < userOpenResource.getExpiryTime().getTime()) {
			Map<String, Object> map = new HashMap<>();
			Long userId = userOpenResource.getUserId();
			User user = userService.findById(userId);
			List<Map<String, Object>> user24h = poolService.getUser24H(userId);
			map.put("json", objectMapper.writeValueAsString(user24h));
			List<User> list = new ArrayList<>();
			list.add(user);
			UserStatus userStatus = poolService.getMasterRuntimeInfo(list).get(0);
			if (userStatus == null) {
				userStatus = new UserStatus();
			}
			map.put("workerOnline", userStatus.getWorkerActive());
			map.put("workerOffline", userStatus.getOffLine());
			result = GsonUtil.getGson().toJson(map);
		}
		return result;
	}

	@RequestMapping("/rest/id")
	@ApiOperation("获取分享数据")//分享页面没有调用这个接口的30天算力的
	@ResponseBody
	public Result restInfo() throws JsonProcessingException {
		String id = ParseRequestUtils.getStringByKey("id");

		UserOpenResource userOpenResource = userOpenResourceService.findById(id);
		if(userOpenResource == null){
			log.error(">>>>>>服务[{}]分享 share/rest/id,id[{}]不存在.....", multipleProperties.getName(), id);
			return  Result.ok();
		}
		Date date = new Date();
		Map<String, Object> result = new HashMap<>();
		if (userOpenResource != null && userOpenResource.getExpiryTime() != null
				&& date.getTime() < userOpenResource.getExpiryTime().getTime()) {
			Long userId = userOpenResource.getUserId();
			//用户24小时算力
			List<Map<String, Object>> user24h = poolService.getUser24H(userId);
			//用户30算力处理数据后
			List<StatsUsersDay> user30dList = poolService.getUser30DayShare(userId);
//排序，默认升序
			user30dList= user30dList.stream().sorted(Comparator.comparing(StatsUsersDay::getDay)).collect(Collectors.toList());

			List<UserShareChart> lis = new ArrayList<>();
			for (StatsUsersDay statsUsersDay : user30dList) {
				UserShareChart userShareChart = new UserShareChart();
				userShareChart.setDate(statsUsersDay.getDay());
				userShareChart.setHashAcceptT(statsUsersDay.getHashAcceptT());
				userShareChart.setHashRejectT(statsUsersDay.getHashRejectT());
				userShareChart.getRejectRate();
				lis.add(userShareChart);
			}
			User user = userService.findById(userOpenResource.getUserId());
			List<User> list = new ArrayList<>();
			list.add(user);
			UserStatus userStatus = poolService.getMasterRuntimeInfo(list).get(0);
			if (userStatus == null) {
				userStatus = new UserStatus();
			}

			date = DateUtil.addHour(date, -1);
			String end = DateUtil.getYYYYMMddHH(date);
			date = DateUtil.addHour(date, -24);
			String start = DateUtil.getYYYYMMddHH(date);
			List<Map<String, Object>> data = poolService.getWorker24Online(userId, start, end);

			result.put("share24h", user24h);
			result.put("userStatus", userStatus);
			result.put("user30Share",lis);//用户端分享页增加30天算力
			result.put("worker24hOnline", data); //24小时在线矿工
		}
		return Result.ok(result);
	}
	@RequestMapping("/shareWorkerList")
	@ApiOperation("矿工列表(传分享Id)")
	@ResponseBody
	public Result shareWorkerList(){
		PageModel pageModel = ParseRequestUtils.getObjectValue(PageModel.class);
		Worker worker = ParseRequestUtils.getObjectValue(Worker.class);
		Constant.WorkerStatus status = Constant.WorkerStatus.valueOf(ParseRequestUtils.getStringByKey("status"));
		String id = ParseRequestUtils.getStringByKey("id");

		log.debug("param is pageModel =>{} ,Worker => {},WorkerStatus => {} id => {}",pageModel,worker,status,id);
		UserOpenResource userOpenResource = userOpenResourceService.findById(id);
		if(userOpenResource == null){
			log.error(">>>>>>服务[{}]分享 share/shareWorkerList,id[{}]不存在.....", multipleProperties.getName(), id);
			return  Result.ok();
		}
		Long userId = userOpenResource.getUserId();

		IPage<Long> page = new Page<>(pageModel);
		if (worker == null) {
			worker = new Worker();
		}
		if (status == null) {
			status = Constant.WorkerStatus.all;
		}
		worker.setUserId(userId);
		worker.setWorkerStatus(status.getStatus());
		IPage<Map<String, Object>> selectWorker = workerService.selectWorker(page, worker, null);
		return Result.ok(selectWorker);
	}


	@RequestMapping("/pay/record")
	@ApiOperation("获得支付记录-分享頁")
	@ResponseBody
	public Result getPayRecord() {
		PageModel model = ParseRequestUtils.getObjectValue(PageModel.class);
		String id = ParseRequestUtils.getStringByKey("id");


		IPage<Map<String, Object>> page = new Page<>(model);

		UserPayBillItem	wallet = new UserPayBillItem();

		UserOpenResource userOpenResource = userOpenResourceService.findById(id);
		if(userOpenResource == null){
			log.error(">>>>>>服务[{}]分享 share/pay/record,id[{}]不存在.....", multipleProperties.getName(), id);
			return  Result.ok();
		}
		Long userId = userOpenResource.getUserId();
//		User user = AccountSecurityUtils.getUser();
		wallet.setPuid(userId);
		IPage<Map<String, Object>> pages = userPayBillItemService.getPayRecord(page, wallet);
		return Result.ok(pages);
	}

	@RequestMapping("/getUser30Day/id")
	@ApiOperation("获取用户30天 算力")
	@ResponseBody
	public Result getUser30Day() {
		String userId = ParseRequestUtils.getStringByKey("id");

		// 获取当前登录用户
//		User user = AccountSecurityUtils.getUser();
//		Long userId = user.getUserId();
		log.debug("userId------->"+userId);
		if (userId !=null){
			Long id = Long.valueOf(userId);//返回Long包装类型
			log.debug("id:"+id);
			List<StatsUsersDay> share30 = poolService.getUser30DayShare(id);
			return Result.ok(share30);
		}
		return Result.ok();
	}

	@RequestMapping("/workerGroupList")
	@ApiOperation("矿工组列表")
	//@RequiresPermissions("son")
	@ResponseBody
	public Result listWorkerGroupBtc() {
		PageModel pageModel = ParseRequestUtils.getObjectValue(PageModel.class);
		WorkerGroupParamModel paramModel = ParseRequestUtils.getObjectValue(WorkerGroupParamModel.class);
		String id = ParseRequestUtils.getStringByKey("id");

		UserOpenResource userOpenResource = userOpenResourceService.findById(id);
		if(userOpenResource == null){
			log.error(">>>>>>服务[{}]分享 share/workerGroupList,id[{}]不存在.....", multipleProperties.getName(), id);
			return  Result.ok();
		}
		WorkerGroup workerGroup = new WorkerGroup();

		//User user = AccountSecurityUtils.getUser();
		//workerGroup.setUserId(user.getUserId());
		workerGroup.setUserId(userOpenResource.getUserId());

		workerGroup.setGroupName(paramModel.getGroupName());
		List<WorkerGroup> list = workerService.listWorkerGroup(workerGroup);
		return Result.ok(list);
	}

	@RequestMapping("/workerList")
	@ApiOperation("矿工列表")
	//@RequiresPermissions("son")
	@ResponseBody
	public Result listWorkerBtc() {
		PageModel pageModel = ParseRequestUtils.getObjectValue(PageModel.class);
		Worker worker = ParseRequestUtils.getObjectValue(Worker.class);
		Constant.WorkerStatus status = Constant.WorkerStatus.valueOf(ParseRequestUtils.getStringByKey("status"));
		Integer groupId = ParseRequestUtils.getIntegerValueByKey("groupId");
		String id = ParseRequestUtils.getStringByKey("id");

		UserOpenResource userOpenResource = userOpenResourceService.findById(id);
		if(userOpenResource == null){
			log.error(">>>>>>服务[{}]分享 share/workerList,id[{}]不存在.....", multipleProperties.getName(), id);
			return  Result.ok();
		}
		IPage<Long> page = new Page<>(pageModel);
		if (worker == null) {
			worker = new Worker();
		}
		if (status == null) {
			status = Constant.WorkerStatus.all;
		}
		if (groupId != null && groupId == 0) {
			groupId = null;
		}

		//User user = AccountSecurityUtils.getUser();
		//worker.setUserId(user.getUserId());
		worker.setUserId(userOpenResource.getUserId());

		worker.setWorkerStatus(status.getStatus());
		IPage<Map<String, Object>> selectWorker = workerService.selectWorker(page, worker, groupId);
		return Result.ok(selectWorker);
	}

	@RequestMapping("/forward/id")
	@ApiOperation("分享请求转发")
	public String shareForward(HttpServletRequest request){
		String id = ParseRequestUtils.getStringByKey("id");
		String url = ParseRequestUtils.getStringByKey("url");

		UserOpenResource userOpenResource = userOpenResourceService.findById(id);
		Date date = new Date();
		Map<String, Object> result = new HashMap<>();
		if (userOpenResource != null && userOpenResource.getExpiryTime() != null
				&& date.getTime() < userOpenResource.getExpiryTime().getTime()) {
			request.setAttribute("shareAddUserId", userOpenResource.getUserId());
			return "forward:/" + url;
		}

		return Result.err().toString();
	}

}
