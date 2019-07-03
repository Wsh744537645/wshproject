package com.mpool.admin.module.system.controller;

import java.util.Arrays;
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

import com.mpool.admin.module.system.service.SysRoleSysMenuService;
import com.mpool.common.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("角色和菜单")
@RequestMapping(value = { "/apis/role/menu", "/role/menu" })
public class SysRoleSysMenuController {
	@Autowired
	private SysRoleSysMenuService sysRoleSysMenuService;

	@PostMapping("/add/{roleId}")
	@ApiOperation("角色添加菜单")
	public Result roleMenu(@PathVariable(value = "roleId") Integer roleId, @RequestBody Integer[] menuIds) {
		sysRoleSysMenuService.insertRoleMenus(roleId, Arrays.asList(menuIds));
		return Result.ok();
	}

	@DeleteMapping("/del/role/menu/all")
	@ApiOperation("删除角色下面所有菜单")
	public Result deleteRoleAllMenu(@RequestParam(value = "roleId") Integer roleId) {
		sysRoleSysMenuService.deleteRoleAllMenu(roleId);
		return Result.ok();
	}

	@DeleteMapping("/del")
	@ApiOperation("删除角色下面所有菜单")
	public Result deleteRoleMenu(@RequestParam(value = "id") Integer id) {
		sysRoleSysMenuService.delete(id);
		return Result.ok();
	}

	@GetMapping("/get/{roleId}")
	@ApiOperation("查询该角色下面有哪些菜单")
	public Result getMenuByRole(@PathVariable(value = "roleId") Integer roleId) {
		List<Map<String, Object>> list = sysRoleSysMenuService.getMenuByRole(roleId);
		return Result.ok(list);
	}
}
