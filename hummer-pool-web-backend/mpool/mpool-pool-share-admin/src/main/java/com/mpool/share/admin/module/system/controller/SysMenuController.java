package com.mpool.share.admin.module.system.controller;

import com.mpool.share.admin.module.system.model.MenuModel;
import com.mpool.share.admin.module.system.service.SysMenuService;
import com.mpool.common.Result;
import com.mpool.common.annotation.CurrentUser;
import com.mpool.common.model.admin.SysMenu;
import com.mpool.common.model.admin.SysUser;
import com.mpool.common.model.aspect.annotation.Update;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = { "/sys/menu", "apis/sys/menu" })
@Api("菜单")
public class SysMenuController {
	@Autowired
	private SysMenuService menuService;

	@PostMapping("/add")
	@ApiOperation("添加菜单")
	@Update
	public Result addMenu(@RequestBody SysMenu sysMenu, @CurrentUser SysUser sysUser) {
		sysMenu.setCreateBy(sysUser.getUserId());
		sysMenu.setCreateTime(new Date());
		menuService.insert(sysMenu);
		return Result.ok();
	}

	@PostMapping("update")
	@ApiOperation("修改菜单")
	public Result updateMenu(@RequestBody SysMenu sysMenu, @CurrentUser SysUser sysUser) {
		
		SysMenu sysMenu2 = new SysMenu();
		sysMenu2.setLastupdateBy(sysUser.getLastupdateBy());
		sysMenu2.setLastupdateTime(new Date());
		sysMenu2.setMenuId(sysMenu.getMenuId());
		sysMenu2.setText(sysMenu.getText());
		menuService.update(sysMenu2);
		return Result.ok();
	}

	@GetMapping("/menus")
	@ApiOperation("获取菜单和语言")
	public Result getMenus(@CurrentUser SysUser user) {
		List<SysMenu> sysMenus = menuService.findByUserId(user.getUserId());

		Set<MenuModel> menus = menuService.transformMenuModel(sysMenus);
		// Map<String, Object> userLang =
		// sysLangService.getUserLang(user.getLangType());
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("menu", menus);
		return Result.ok(result);
	}

	@GetMapping("/list")
	@ApiOperation("获得所有菜单")
	public Result getList() {
		List<SysMenu> list = menuService.getList();
		return Result.ok(list);
	}

}
