package com.mpool.account.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.mpool.account.service.UserOpenResourceService;
import com.mpool.account.service.UserService;
import com.mpool.common.model.account.UserOpenResource;
import com.mpool.common.properties.MultipleProperties;
import com.mpool.common.util.GsonUtil;
import com.mpool.rpc.RpcInstance;
import com.mpool.rpc.account.producer.btc.MultipleShare;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.account.constant.Constant.WorkerStatus;
import com.mpool.account.model.WorkerGroupParamModel;
import com.mpool.account.model.WorkerParamModel;
import com.mpool.account.service.WorkerService;
import com.mpool.account.utils.AccountSecurityUtils;
import com.mpool.common.Result;
import com.mpool.common.model.account.User;
import com.mpool.common.model.account.Worker;
import com.mpool.common.model.account.WorkerGroup;
import com.mpool.common.model.pool.MiningWorkers;
import com.mpool.common.util.CSVUtil;
import com.mpool.common.util.DateUtil;
import com.mpool.common.util.ExcelUtil;
import com.mpool.common.util.page.Page;
import com.mpool.common.util.page.PageModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
@RequestMapping({ "/worker", "/apis/worker" })
@Api("矿工管理")
@Slf4j
public class WorkerController {
	@Autowired
	private WorkerService workerService;
	@Autowired
	private MultipleProperties multipleProperties;
	@Autowired
	private RpcInstance rpcInstance;
	@Autowired
	private UserOpenResourceService userOpenResourceService;
	@Autowired
	private UserService userService;

	@PostMapping("/createWorkerGroup")
	@ApiOperation("创建矿工组")
	@RequiresPermissions("son")
	public String createWorkerGroup(){
		String currencyName = AccountSecurityUtils.getCurPayCurrencyName();
		if(currencyName == null || currencyName.equals(multipleProperties.getName())) {
			return "forward:/worker/createWorkerGroupbtc";
		}
		else{

			return "forward:/mutiple/" + currencyName + "?url=worker/createWorkerGroup";
		}
	}

	@PostMapping("/createWorkerGroupbtc")
	@ApiOperation("创建矿工组")
	@ResponseBody
	public Result createWorkerGroupBtc(@Valid @RequestBody WorkerGroupParamModel workerGroupParam) {
		User currUser = AccountSecurityUtils.getUser();
		WorkerGroup workerGroup = new WorkerGroup();
		workerGroup.setGroupName(workerGroupParam.getGroupName());
		workerGroup.setGroupSeq(workerGroupParam.getGroupSeq());
		workerGroup.setUserId(currUser.getUserId());
		workerService.createWorkerGruop(workerGroup);
		return Result.ok();
	}

	@DeleteMapping("/deleteWorkerGroup")
	@ApiOperation("删除组")
	@RequiresPermissions("son")
	public String deleteWorkerGroup(){
		String currencyName = AccountSecurityUtils.getCurPayCurrencyName();
		if(currencyName == null || currencyName.equals(multipleProperties.getName())) {
			return "forward:/worker/deleteWorkerGroupbtc";
		}
		else{

			return "forward:/mutiple/" + currencyName + "?url=worker/deleteWorkerGroup";
		}
	}

	@DeleteMapping("/deleteWorkerGroupbtc")
	@ApiOperation("删除组")
	@ResponseBody
	public Result deleteWorkerGroupBtc(@RequestParam(value = "groupId") Long groupId) {
		User currUser = AccountSecurityUtils.getUser();
		workerService.deleteWorkerGruop(groupId, currUser.getUserId());
		return Result.ok();
	}

	@DeleteMapping("/deleteWorker")
	@ApiOperation("删除矿工")
	@RequiresPermissions("son")
	public String deleteWorker(){
		String currencyName = AccountSecurityUtils.getCurPayCurrencyName();
		if(currencyName == null || currencyName.equals(multipleProperties.getName())) {
			return "forward:/worker/deleteWorkerbtc";
		}
		else{

			return "forward:/mutiple/" + currencyName + "?url=worker/deleteWorker";
		}
	}

	@DeleteMapping("/deleteWorkerbtc")
	@ApiOperation("删除矿工")
	@ResponseBody
	public Result deleteWorkerBtc(@RequestParam(value = "workerId") Long workerId) {
		User currUser = AccountSecurityUtils.getUser();
		workerService.deleteWorker(workerId, currUser.getUserId());
		return Result.ok();
	}

	@PostMapping("/deleteWorkerBatch")
	@ApiOperation("删除矿工（批量删除）->参数：Long数组")
	@RequiresPermissions("son")
	public String deleteWorkerBatch(){
		String currencyName = AccountSecurityUtils.getCurPayCurrencyName();
		if(currencyName == null || currencyName.equals(multipleProperties.getName())) {
			return "forward:/worker/deleteWorkerBatchbtc";
		}
		else{

			return "forward:/mutiple/" + currencyName + "?url=worker/deleteWorkerBatch";
		}
	}

	@PostMapping("/deleteWorkerBatchbtc")
	@ApiOperation("删除矿工（批量删除）->参数：Long数组")
	@RequiresPermissions("son")
	@ResponseBody
	public Result deleteWorkerBatchBtc(@RequestParam(value = "userId", required = false) String userId,
			@RequestBody Long[] workerIds) {
		User currUser = AccountSecurityUtils.getUser();
		workerService.deleteWorkerBatch(currUser.getUserId() + "", workerIds);
		return Result.ok();
	}

	@PostMapping("/updateWorkerGroup")
	@ApiOperation("修改矿工组信息")
	@RequiresPermissions("son")
	public String updateWorkerGroup(){
		String currencyName = AccountSecurityUtils.getCurPayCurrencyName();
		if(currencyName == null || currencyName.equals(multipleProperties.getName())) {
			return "forward:/worker/updateWorkerGroupbtc";
		}
		else{

			return "forward:/mutiple/" + currencyName + "?url=worker/updateWorkerGroup";
		}
	}

	@PostMapping("/updateWorkerGroupbtc")
	@ApiOperation("修改矿工组信息")
	@RequiresPermissions("son")
	@ResponseBody
	public Result updateWorkerGroupBtc(@Valid @RequestBody WorkerGroupParamModel workerGroupParam,
			@RequestParam(value = "groupId") Long groupId) {
		User currUser = AccountSecurityUtils.getUser();
		WorkerGroup workerGroup = new WorkerGroup();
		workerGroup.setGroupId(groupId);
		workerGroup.setGroupName(workerGroupParam.getGroupName());
		workerGroup.setGroupSeq(workerGroupParam.getGroupSeq());
		workerGroup.setUserId(currUser.getUserId());
		workerService.updateWorkerGruop(workerGroup);
		return Result.ok();
	}

	@GetMapping("/workerList")
	@ApiOperation("矿工列表")
	public String listWorker(){
		String currencyName = AccountSecurityUtils.getCurPayCurrencyName();
		if(currencyName == null || currencyName.equals(multipleProperties.getName())) {
			return "forward:/worker/workerListbtc";
		}
		else{

			return "forward:/mutiple/" + currencyName + "?url=worker/workerList";
		}
	}

	@GetMapping("/workerListbtc")
	@ApiOperation("矿工列表")
	//@RequiresPermissions("son")
	@ResponseBody
	public Result listWorkerBtc(PageModel pageModel, Worker worker, WorkerStatus status,
			@RequestParam(required = false) Integer groupId) {
		IPage<Long> page = new Page<>(pageModel);
		if (worker == null) {
			worker = new Worker();
		}
		if (status == null) {
			status = WorkerStatus.all;
		}
		if (groupId != null && groupId == 0) {
			groupId = null;
		}

		//User user = AccountSecurityUtils.getUser();
		//worker.setUserId(user.getUserId());
		worker.setUserId(AccountSecurityUtils.getCurrentUserId());

		worker.setWorkerStatus(status.getStatus());
		IPage<Map<String, Object>> selectWorker = workerService.selectWorker(page, worker, groupId);
		return Result.ok(selectWorker);
	}

	@GetMapping(value = "/workerList/export")
	@ApiOperation("导出矿工列表")
	@RequiresPermissions("son")
	@ResponseBody
	public void exportWorker(HttpServletResponse response, String id) throws IOException {
		User user;
		if(id == null) {
			user = AccountSecurityUtils.getUser();
		}else{
			UserOpenResource userOpenResource = userOpenResourceService.findById(id);
			user = userService.findById(userOpenResource.getUserId());
		}
		String fileName = user.getUsername() + "_" + DateUtil.getYYYYMMddHHmmss(new Date());
		List<List<Object>> dataList = new ArrayList<>();

		String currencyName = AccountSecurityUtils.getCurPayCurrencyName();
		if(currencyName == null || currencyName.equals(multipleProperties.getName())) {
			dataList = workerService.exportWorker(user.getUserId());
		}
		else{
			MultipleShare multipleShare = rpcInstance.getRpcInstanceByCurrencyType(currencyName);
			if(multipleShare != null){
				String result = multipleShare.exportWorker(user);
				if(result != null){
					dataList = GsonUtil.getGson().fromJson(result, List.class);
				}else {
					log.error(">>>>多币服务[{}]被熔断，导出矿工列表失败！", currencyName);
				}
			}else{
				log.error(">>>>多币服务[{}]不存在，导出矿工列表失败！", currencyName);
			}
		}

		response.setHeader("content-type", "application/octet-stream;charset=UTF-8");
		response.setContentType("application/octet-stream");
		// 下载文件能正常显示中文
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
		Object[] he = { "矿工名", "15分钟平均算力", "1小时平均算力", "15分钟拒绝率", "1小时拒绝率", "最近提交时间", "矿机状态" };
		List<Object> head = Arrays.asList(he);
		HSSFWorkbook hssfWorkbook = ExcelUtil.getHSSFWorkbook("default", head, dataList, null);
		hssfWorkbook.write(response.getOutputStream());
//		CSVUtil.createCSVFile(head, dataList, response.getOutputStream());

	}

	@GetMapping("/workerGroupList")
	@ApiOperation("矿工组列表")
	public String listWorkerGroup(){
		String currencyName = AccountSecurityUtils.getCurPayCurrencyName();
		if(currencyName == null || currencyName.equals(multipleProperties.getName())) {
			return "forward:/worker/workerGroupListbtc";
		}
		else{

			return "forward:/mutiple/" + currencyName + "?url=worker/workerGroupList";
		}
	}

	@GetMapping("/workerGroupListbtc")
	@ApiOperation("矿工组列表")
	//@RequiresPermissions("son")
	@ResponseBody
	public Result listWorkerGroupBtc(PageModel pageModel, WorkerGroupParamModel paramModel) {
		WorkerGroup workerGroup = new WorkerGroup();

		//User user = AccountSecurityUtils.getUser();
		//workerGroup.setUserId(user.getUserId());
		workerGroup.setUserId(AccountSecurityUtils.getCurrentUserId());

		workerGroup.setGroupName(paramModel.getGroupName());
		List<WorkerGroup> list = workerService.listWorkerGroup(workerGroup);
		return Result.ok(list);
	}

	@PostMapping("/change/group")
	@ApiOperation("修改矿工的组（移动矿工）")
	@RequiresPermissions("son")
	public String changeGroup(){
		String currencyName = AccountSecurityUtils.getCurPayCurrencyName();
		if(currencyName == null || currencyName.equals(multipleProperties.getName())) {
			return "forward:/worker/change/groupbtc";
		}
		else{

			return "forward:/mutiple/" + currencyName + "?url=worker/change/group";
		}
	}

	@PostMapping("/change/groupbtc")
	@ApiOperation("修改矿工的组（移动矿工）")
	@RequiresPermissions("son")
	@ResponseBody
	public Result changeGroupBtc(@RequestParam(value = "groupId") Long groupId, @RequestBody String[] workerIds) {
		workerService.changeGroup(groupId, Arrays.asList(workerIds));
		return Result.ok();
	}

	@GetMapping("/check/groupName")
	@ApiOperation("检测组名是否重复")
	@RequiresPermissions("son")
	public String checkExistByWorkerGroupName(){
		String currencyName = AccountSecurityUtils.getCurPayCurrencyName();
		if(currencyName == null || currencyName.equals(multipleProperties.getName())) {
			return "forward:/worker/check/groupNamebtc";
		}
		else{

			return "forward:/mutiple/" + currencyName + "?url=worker/check/groupName";
		}
	}

	@GetMapping("/check/groupNamebtc")
	@ApiOperation("检测组名是否重复")
	@RequiresPermissions("son")
	@ResponseBody
	public Result checkExistByWorkerGroupNameBtc(@RequestParam(value = "groupName") String groupName) {
		User user = (User) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
		workerService.checkExistByWorkerGroupName(user.getUserId(), groupName);
		return Result.ok();
	}

}
