package com.mpool.accountmultiple.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.accountmultiple.utils.AccountUtils;
import com.mpool.accountmultiple.utils.ParseRequestUtils;
import com.mpool.common.util.GsonUtil;
import com.mpool.mpoolaccountmultiplecommon.constant.Constant.WorkerStatus;
import com.mpool.mpoolaccountmultiplecommon.model.WorkerGroupParamModel;
import com.mpool.accountmultiple.service.WorkerService;
import com.mpool.common.Result;
import com.mpool.common.model.account.User;
import com.mpool.common.model.account.Worker;
import com.mpool.common.model.account.WorkerGroup;
import com.mpool.common.util.DateUtil;
import com.mpool.common.util.ExcelUtil;
import com.mpool.common.util.page.Page;
import com.mpool.common.util.page.PageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({ "/worker", "/apis/worker" })
@Api("矿工管理")
public class WorkerController {
	@Autowired
	private WorkerService workerService;

	@RequestMapping("/createWorkerGroup")
	@ApiOperation("创建矿工组")
	@RequiresPermissions("son")
	public Result createWorkerGroup() {
		WorkerGroupParamModel workerGroupParam = ParseRequestUtils.getObjectValue(WorkerGroupParamModel.class);

		User currUser = AccountUtils.getUser();
		WorkerGroup workerGroup = new WorkerGroup();
		workerGroup.setGroupName(workerGroupParam.getGroupName());
		workerGroup.setGroupSeq(workerGroupParam.getGroupSeq());
		workerGroup.setUserId(currUser.getUserId());
		workerService.createWorkerGruop(workerGroup);
		return Result.ok();
	}

	@RequestMapping("/deleteWorkerGroup")
	@ApiOperation("删除组")
	@RequiresPermissions("son")
	public Result deleteWorkerGroup() {
		Long groupId = ParseRequestUtils.getLongValueByKey("groupId");
		User currUser = AccountUtils.getUser();
		workerService.deleteWorkerGruop(groupId, currUser.getUserId());
		return Result.ok();
	}

	@RequestMapping("/deleteWorker")
	@ApiOperation("删除矿工")
	@RequiresPermissions("son")
	public Result deleteWorker() {
		Long workerId = ParseRequestUtils.getLongValueByKey("workerId");
		User currUser = AccountUtils.getUser();
		workerService.deleteWorker(workerId, currUser.getUserId());
		return Result.ok();
	}

	@RequestMapping("/deleteWorkerBatch")
	@ApiOperation("删除矿工（批量删除）->参数：Long数组")
	@RequiresPermissions("son")
	public Result deleteWorkerBatch() {
		String userId = ParseRequestUtils.getStringByKey("userId");
		Long[] workerIds = ParseRequestUtils.getGenericConverterLong("workerIds");

		User currUser = AccountUtils.getUser();
		workerService.deleteWorkerBatch(currUser.getUserId() + "", workerIds);
		return Result.ok();
	}

	@RequestMapping("/updateWorkerGroup")
	@ApiOperation("修改矿工组信息")
	@RequiresPermissions("son")
	public Result updateWorkerGroup() {
		WorkerGroupParamModel workerGroupParam = ParseRequestUtils.getObjectValue(WorkerGroupParamModel.class);
		Long groupId = ParseRequestUtils.getLongValueByKey("groupId");

		User currUser = AccountUtils.getUser();
		WorkerGroup workerGroup = new WorkerGroup();
		workerGroup.setGroupId(groupId);
		workerGroup.setGroupName(workerGroupParam.getGroupName());
		workerGroup.setGroupSeq(workerGroupParam.getGroupSeq());
		workerGroup.setUserId(currUser.getUserId());
		workerService.updateWorkerGruop(workerGroup);
		return Result.ok();
	}

	@RequestMapping("/workerList")
	@ApiOperation("矿工列表")
	//@RequiresPermissions("son")
	public Result listWorker() {
		PageModel pageModel = ParseRequestUtils.getObjectValue(PageModel.class);
		Worker worker = ParseRequestUtils.getObjectValue(Worker.class);
		WorkerStatus status = WorkerStatus.valueOf(ParseRequestUtils.getStringByKey("status"));
		Integer groupId = ParseRequestUtils.getIntegerValueByKey("groupId");

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
		worker.setUserId(AccountUtils.getCurrentUserId());

		worker.setWorkerStatus(status.getStatus());
		IPage<Map<String, Object>> selectWorker = workerService.selectWorker(page, worker, groupId);
		return Result.ok(selectWorker);
	}

	@RequestMapping(value = "/workerList/export")
	@ApiOperation("导出矿工列表")
	@RequiresPermissions("son")
	public String exportWorker(@RequestBody User user, HttpServletResponse response) throws IOException {
		String fileName = user.getUsername() + "_" + DateUtil.getYYYYMMddHHmmss(new Date());
		List<List<Object>> dataList = workerService.exportWorker(user.getUserId());

		return GsonUtil.getGson().toJson(dataList);

//		response.setHeader("content-type", "application/octet-stream;charset=UTF-8");
//		response.setContentType("application/octet-stream");
//		// 下载文件能正常显示中文
//		response.setHeader("Content-Disposition",
//				"attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
//		Object[] he = { "矿工名", "15分钟平均算力", "1小时平均算力", "15分钟拒绝率", "1小时拒绝率", "最近提交时间", "矿机状态" };
//		List<Object> head = Arrays.asList(he);
//		HSSFWorkbook hssfWorkbook = ExcelUtil.getHSSFWorkbook("default", head, dataList, null);
//		hssfWorkbook.write(response.getOutputStream());
	}

	@RequestMapping("/workerGroupList")
	@ApiOperation("矿工组列表")
	//@RequiresPermissions("son")
	public Result listWorkerGroup() {
		PageModel pageModel = ParseRequestUtils.getObjectValue(PageModel.class);
		WorkerGroupParamModel paramModel = ParseRequestUtils.getObjectValue(WorkerGroupParamModel.class);

		WorkerGroup workerGroup = new WorkerGroup();

		//User user = AccountSecurityUtils.getUser();
		//workerGroup.setUserId(user.getUserId());
		workerGroup.setUserId(AccountUtils.getCurrentUserId());

		workerGroup.setGroupName(paramModel.getGroupName());
		List<WorkerGroup> list = workerService.listWorkerGroup(workerGroup);
		return Result.ok(list);
	}

	@RequestMapping("/change/group")
	@ApiOperation("修改矿工的组（移动矿工）")
	@RequiresPermissions("son")
	public Result changeGroup() {
		Long groupId = ParseRequestUtils.getLongValueByKey("groupId");
		String[] workerIds = ParseRequestUtils.getGenericConverterByKey("workerIds");

		workerService.changeGroup(groupId, Arrays.asList(workerIds));
		return Result.ok();
	}

	@RequestMapping("/check/groupName")
	@ApiOperation("检测组名是否重复")
	@RequiresPermissions("son")
	public Result checkExistByWorkerGroupName() {
		String groupName = ParseRequestUtils.getStringByKey("groupName");

		User user = (User) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
		workerService.checkExistByWorkerGroupName(user.getUserId(), groupName);
		return Result.ok();
	}

}
