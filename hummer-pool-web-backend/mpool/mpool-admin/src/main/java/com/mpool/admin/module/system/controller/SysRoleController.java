package com.mpool.admin.module.system.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mpool.admin.exception.AdminException;
import com.mpool.admin.module.system.service.SysRoleService;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.common.Result;
import com.mpool.common.model.admin.SysRole;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = { "/sys/role", "/apis/sys/role" })
@Api("系统角色管理")
public class SysRoleController {
	@Autowired
	private SysRoleService roleService;

	@PostMapping("/add")
	@ApiOperation("添加角色")
	public Result addRole(@RequestBody SysRole sysRole) {
		sysRole.setCreateBy(AdminSecurityUtils.getUser().getUserId());
		sysRole.setCreateTime(new Date());
		roleService.insert(sysRole);
		return Result.ok();
	}

	@PostMapping("/update")
	@ApiOperation("编辑角色")
	public Result updateRole(@RequestBody SysRole sysRole) {
		sysRole.setLastupdateBy(AdminSecurityUtils.getUser().getUserId());
		sysRole.setLastupdateTime(new Date());
		if (sysRole.getRoleId() == null) {
			throw new AdminException("role.id.null", "角色ID为空");
		}
		roleService.update(sysRole);
		return Result.ok();
	}

	/**
	 * @param roleId
	 * @return
	 * 
	 */
	@DeleteMapping("/del")
	@ApiOperation("删除角色")
	public Result deleteRole(@RequestParam(value = "roleId") Integer roleId) {
		if (roleId == 1) {
			throw new AdminException("role.del", "该角色不能删除!");
		}
		roleService.delete(roleId);
		return Result.ok();
	}

	@GetMapping("/roleSelect")
	@ApiOperation("获取角色菜单")
	public Result roleSelect() {
		List<Map<String, Object>> maps = roleService.roleSelect();
		return Result.ok(maps);
	}

	@GetMapping("list")
	@ApiOperation("获取角色列表")
	public Result list() {
		List<SysRole> lit = roleService.list();
		return Result.ok(lit);
	}

}
