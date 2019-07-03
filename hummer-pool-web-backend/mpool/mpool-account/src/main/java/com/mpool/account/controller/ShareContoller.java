package com.mpool.account.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpool.account.constant.Constant;
import com.mpool.account.model.WorkerGroupParamModel;
import com.mpool.account.model.dashboard.UserShareChart;
import com.mpool.account.model.dashboard.UserStatus;
import com.mpool.account.service.*;
import com.mpool.account.service.bill.UserPayBillItemService;
import com.mpool.account.utils.AccountSecurityUtils;
import com.mpool.common.Result;
import com.mpool.common.model.account.Currency;
import com.mpool.common.model.account.User;
import com.mpool.common.model.account.UserOpenResource;
import com.mpool.common.model.account.Worker;
import com.mpool.common.model.account.WorkerGroup;
import com.mpool.common.model.account.bill.UserPayBillItem;
import com.mpool.common.model.pool.StatsUsersDay;
import com.mpool.common.properties.MultipleProperties;
import com.mpool.common.util.DateUtil;
import com.mpool.common.util.GsonUtil;
import com.mpool.common.util.page.Page;
import com.mpool.common.util.page.PageModel;
import com.mpool.rpc.RpcInstance;
import com.mpool.rpc.account.producer.btc.MultipleShare;
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
	@Autowired
	private RpcInstance rpcInstance;
	@Autowired
	private CurrencyService currencyService;

	@GetMapping("/{id}")
	public String info(@PathVariable(value = "id") String id, HttpServletRequest request, HttpServletResponse response)
			throws JsonProcessingException {
		request.setAttribute("key", id);
		UserOpenResource userOpenResource = userOpenResourceService.findById(id);
		if(userOpenResource != null) {
			String currencyName = userOpenResource.getCurrencyName();
			if (currencyName.equals(multipleProperties.getName())) {
				Date date = new Date();
				if (userOpenResource != null && userOpenResource.getExpiryTime() != null
						&& date.getTime() < userOpenResource.getExpiryTime().getTime()) {

					Long userId = userOpenResource.getUserId();
					User user = userService.findById(userId);
					List<Map<String, Object>> user24h = poolService.getUser24H(userId);
					request.setAttribute("json", objectMapper.writeValueAsString(user24h));
					List<User> list = new ArrayList<>();
					list.add(user);
					UserStatus userStatus = poolService.getMasterRuntimeInfo(list).get(0);
					if (userStatus == null) {
						userStatus = new UserStatus();
					}
					request.setAttribute("workerOnline", userStatus.getWorkerActive());
					request.setAttribute("workerOffline", userStatus.getOffLine());
				} else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				}
			} else {
				MultipleShare multipleShare = rpcInstance.getRpcInstanceByCurrencyType(currencyName);
				if (multipleShare != null) {
					String result = multipleShare.info(id);
					if (result != null) {
						Map<String, Object> map = GsonUtil.getGson().fromJson(result, Map.class);
						Set<String> keys = map.keySet();
						for (String key : keys) {
							request.setAttribute(key, map.get(key));
						}
					} else {
						log.error(">>>>多币服务[{}]被熔断，访问分享数据失败！", currencyName);
						response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					}
				} else {
					log.error(">>>>多币服务[{}]不存在，访问分享数据失败！", currencyName);
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				}
			}
		}else{
			log.error(">>>>>>分享 share/info,id[{}]不存在.....", id);
		}
		return "mandate";
	}

	@GetMapping("/rest/{id}")
	@ApiOperation("获取分享数据")//分享页面没有调用这个接口的30天算力的
	public String restInfo(@PathVariable(value = "id") String id){
		UserOpenResource userOpenResource = userOpenResourceService.findById(id);
		if(userOpenResource == null || userOpenResource.getCurrencyName().equals(multipleProperties.getName())){
			return "forward:/share/rest/id?id=" + id;
		}else{
			return "forward:/mutiple/" + userOpenResource.getCurrencyName() + "?url=share/rest/id&id=" + id;
		}
	}

	@GetMapping("/rest/id")
	@ApiOperation("获取分享数据")//分享页面没有调用这个接口的30天算力的
	@ResponseBody
	public Result restInfoBtc(@RequestParam(value = "id") String id) throws JsonProcessingException {

		UserOpenResource userOpenResource = userOpenResourceService.findById(id);
		if(userOpenResource != null){
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
		}else{
			log.error(">>>>>>分享 share/rest/id,id[{}]不存在.....", id);
			return Result.ok();
		}
	}

	@GetMapping("/shareWorkerList")
	@ApiOperation("矿工列表(传分享Id)")
	public String shareWorkerList(@RequestParam("id") String id){
		UserOpenResource userOpenResource = userOpenResourceService.findById(id);
		if(userOpenResource == null || userOpenResource.getCurrencyName().equals(multipleProperties.getName())){
			return "forward:/share/shareWorkerListbtc";
		}else{
			return "forward:/mutiple/" + userOpenResource.getCurrencyName() + "?url=share/shareWorkerList";
		}
	}

	@GetMapping("/shareWorkerListbtc")
	@ApiOperation("矿工列表(传分享Id)")
	@ResponseBody
	public Result shareWorkerListBtc(PageModel pageModel, Worker worker, Constant.WorkerStatus status,
							 @RequestParam("id") String id) {
		log.debug("param is pageModel =>{} ,Worker => {},WorkerStatus => {} id => {}",pageModel,worker,status,id);
		UserOpenResource userOpenResource = userOpenResourceService.findById(id);
		if(userOpenResource == null){
			log.error(">>>>>>分享 share/shareWorkerListbtc,id[{}]不存在.....", id);
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

	@GetMapping("/pay/record")
	@ApiOperation("获得支付记录-分享頁")
	public String getPayRecord(@RequestParam(value = "id")String id){
		UserOpenResource userOpenResource = userOpenResourceService.findById(id);
		if(userOpenResource == null || userOpenResource.getCurrencyName().equals(multipleProperties.getName())){
			return "forward:/share/pay/recordbtc";
		}else{
			return "forward:/mutiple/" + userOpenResource.getCurrencyName() + "?url=share/pay/record";
		}
	}

	@GetMapping("/pay/recordbtc")
	@ApiOperation("获得支付记录-分享頁")
	@ResponseBody
	public Result getPayRecordBtc(PageModel model,@RequestParam(value = "id")String id) {
		IPage<Map<String, Object>> page = new Page<>(model);

		UserPayBillItem	wallet = new UserPayBillItem();

		UserOpenResource userOpenResource = userOpenResourceService.findById(id);
		if(userOpenResource == null){
			log.error(">>>>>>分享 share/pay/recordbtc,id[{}]不存在.....", id);
			return  Result.ok();
		}
		Long userId = userOpenResource.getUserId();
//		User user = AccountSecurityUtils.getUser();
		wallet.setPuid(userId);
		IPage<Map<String, Object>> pages = userPayBillItemService.getPayRecord(page, wallet);
		return Result.ok(pages);
	}

	@GetMapping("/getUser30Day/{userId}")
	@ApiOperation("获取用户30天 算力")
	public String getUser30Day(@RequestParam(value = "userId")String userId, String currencyName){
		if(currencyName == null || currencyName.equals(multipleProperties.getName())){
			return "forward:/share/getUser30Day/userId?userId="+userId;
		}else{
			return "forward:/mutiple/" + currencyName + "?url=share/getUser30Day/id&id=" + userId;
		}
	}

	@GetMapping("/getUser30Day/userId")
	@ApiOperation("获取用户30天 算力")
	@ResponseBody
	public Result getUser30DayBtc(@PathVariable(value = "userId")String userId) {
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

	@GetMapping("/workerGroupList")
	@ApiOperation("矿工组列表")
	public String listWorkerGroup(@RequestParam(value = "id")String id){
		UserOpenResource userOpenResource = userOpenResourceService.findById(id);
		if(userOpenResource == null || userOpenResource.getCurrencyName() == null || userOpenResource.getCurrencyName().equals(multipleProperties.getName())) {
			return "forward:/share/workerGroupListbtc";
		}
		else{

			return "forward:/mutiple/" + userOpenResource.getCurrencyName() + "?url=share/workerGroupList";
		}
	}

	@GetMapping("/workerGroupListbtc")
	@ApiOperation("矿工组列表")
	//@RequiresPermissions("son")
	@ResponseBody
	public Result listWorkerGroupBtc(PageModel pageModel, WorkerGroupParamModel paramModel,@RequestParam(value = "id")String id) {
		UserOpenResource userOpenResource = userOpenResourceService.findById(id);

		if(userOpenResource == null){
			log.error(">>>>>>分享 share/workerGroupListbtc,id[{}]不存在.....", id);
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

	@GetMapping("/workerList")
	@ApiOperation("矿工列表")
	public String listWorker(@RequestParam(value = "id")String id){
		UserOpenResource userOpenResource = userOpenResourceService.findById(id);
		if(userOpenResource == null || userOpenResource.getCurrencyName() == null || userOpenResource.getCurrencyName().equals(multipleProperties.getName())) {
			return "forward:/share/workerListbtc";
		}
		else{

			return "forward:/mutiple/" + userOpenResource.getCurrencyName() + "?url=share/workerList";
		}
	}

	@GetMapping("/workerListbtc")
	@ApiOperation("矿工列表")
	//@RequiresPermissions("son")
	@ResponseBody
	public Result listWorkerBtc(PageModel pageModel, Worker worker, Constant.WorkerStatus status,
								@RequestParam(required = false) Integer groupId, @RequestParam(value = "id")String id) {
		UserOpenResource userOpenResource = userOpenResourceService.findById(id);

		if(userOpenResource == null){
			log.error(">>>>>>分享 share/workerListbtc,id[{}]不存在.....", id);
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

	@GetMapping("/getCurrencyList")
	@ApiOperation("获得当前支持的币种列表")
	@ResponseBody
	public Result getCurrencyList(){
		List<Currency> currencies = currencyService.selectListByEnable();
		return Result.ok(currencies);
	}

	@GetMapping("/forward/{id}")
	@ApiOperation("分享请求转发") // 这个接口暂时没有用
	public String shareForward(@PathVariable(value = "id") String id, @RequestParam(value = "url") String url, HttpServletRequest request){
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
