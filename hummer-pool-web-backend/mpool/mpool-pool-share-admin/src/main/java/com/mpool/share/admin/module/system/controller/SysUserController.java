package com.mpool.share.admin.module.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.share.admin.module.log.service.LogAdminOperationService;
import com.mpool.share.admin.module.system.mapper.SysRoleMapper;
import com.mpool.share.admin.module.system.service.SysUserService;
import com.mpool.share.admin.utils.AdminSecurityUtils;
import com.mpool.common.Result;
import com.mpool.common.annotation.CurrentUser;
import com.mpool.common.model.admin.SysRole;
import com.mpool.common.model.admin.SysUser;
import com.mpool.common.model.admin.SysUserSysRole;
import com.mpool.common.model.log.LogUserOperation;
import com.mpool.common.util.EncryUtil;
import com.mpool.common.util.page.Page;
import com.mpool.common.util.page.PageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@RestController
@RequestMapping(value = { "sys/user", "/apis/sys/user" })
@Api("SysUser管理")
public class SysUserController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private ExecutorService executorService;
	@Autowired
	private LogAdminOperationService userOperationService;
	@Autowired
	SysRoleMapper sysRoleMapper;

	@PostMapping("/add")
	public Result addSysUser(@RequestBody SysUser sysUser, @RequestParam("roleId") Integer roleId) {
		//sysUser.setStatus(1);//1正常admin人员
		sysUserService.insert(sysUser, roleId);
		//记录日志
		SysUser user = AdminSecurityUtils.getUser();//获取当前登录的管理员
		executorService.execute(() -> {
				if (user != null) {
				SysRole role = sysRoleMapper.selectById(roleId);
				String roleName = role.getRoleName();
					LogUserOperation model = new LogUserOperation();
					model.setLogType(3);//3是系统日志
					model.setUserType("admin");//操作人类型是管理员
					model.setCreatedTime(new Date());
					model.setUserId(user.getUserId());
					model.setContent("管理员[" + user.getUsername() + "]-添加系统用户[" + sysUser.getUsername() + "],角色为["+roleName+"]操作成功");
					userOperationService.insert(model);
				}
		});
		return Result.ok();
	}

	@PostMapping("/change/user/role")
	@ApiOperation("修改角色权限")
	public Result changeUserRole(Long userId, Integer roleId) {
		SysUserSysRole userRole = new SysUserSysRole();
		SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
		userRole.setUserId(userId);
		userRole.setRoleId(roleId);
		userRole.setCreateBy(user.getUserId());
		userRole.setCreateTime(new Date());
		sysUserService.changeUserRole(userRole);
		return Result.ok();
	}

	@GetMapping("/list/page")
	@ApiOperation("用户列表")
	public Result list(PageModel model, SysUser sysUser) {
		IPage<SysUser> iPage = new Page<>(model);
		IPage<Map<String, Object>> list = sysUserService.listPage(iPage, sysUser);
		return Result.ok(list);
	}

	@PostMapping("/reset/password")
	@ApiOperation("系统管理员-修改密码")
	public Result updatePassword(@CurrentUser SysUser sysUser, String oldPassword, String newPassword) {
		sysUserService.updatePassword(sysUser, oldPassword, newPassword);
		return Result.ok();
	}

	@PostMapping("/update")
	@ApiOperation("修改用户信息")
	public Result updateUserInfo(@RequestBody SysUser sysUser, @CurrentUser SysUser cUser) {
		SysUser entity = new SysUser();
		Long userId = cUser.getUserId();
		entity.setUserId(userId);
		entity.setTelphone(sysUser.getTelphone());
		entity.setEmail(sysUser.getEmail());
		entity.setLastupdateTime(new Date());
		entity.setLastupdateBy(userId);
		sysUserService.update(entity);
		return Result.ok();
	}

	@PostMapping("/admin/update")
	@ApiOperation("系统管理员修改用户信息")
	public Result adminUpdateUserInfo(@RequestBody SysUser sysUser, @CurrentUser SysUser cUser) {
		SysUser entity = new SysUser();
		Long currentUserId = cUser.getUserId();
		Long userId = sysUser.getUserId();
		entity.setUserId(userId);
		entity.setTelphone(sysUser.getTelphone());
		entity.setEmail(sysUser.getEmail());
		entity.setLastupdateTime(new Date());
		entity.setLastupdateBy(currentUserId);
		if(sysUser.getPassword() != null && !sysUser.getPassword().isEmpty()) {
			entity.setPassword(EncryUtil.encrypt(sysUser.getPassword()));
		}
		sysUserService.update(entity);
		return Result.ok();
	}

	@GetMapping("/info")
	@ApiOperation("获得自己的信息")
	public Result getSysUserInfo(@CurrentUser SysUser cUser) {
		SysUser findById = sysUserService.findById(cUser.getUserId());
		findById.setPassword(null);
		return Result.ok(findById);
	}

	@GetMapping("/admin/info/{id}")
	@ApiOperation("系统管理员获得账号的信息")
	public Result getSysUserInfo(@PathVariable(value = "id") Long userId) {
		SysUser findById = sysUserService.findById(userId);
		findById.setPassword(null);
		return Result.ok(findById);
	}

	@GetMapping("/del/user")
	@ApiOperation("删除用户")
	public Result delSysUserInfo(@RequestParam("userId") Long userId) {
		sysUserService.deleteByUserId(userId);
		return Result.ok();
	}

	@PostMapping("/updateAdminStatus")
	@ApiOperation("修改系统管理员的状态0为锁定不能再登录")
	public Result updateAdminStatus(@RequestParam("adminId")String adminId) {
		SysUser sysUser = sysUserService.findByUserById(adminId);
		if (sysUser != null){
			//sysUser.setStatus(0);//0为锁定不能再登录
			sysUserService.update(sysUser);
		}
		return Result.ok();
	}
}
