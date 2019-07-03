package com.mpool.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mpool.account.service.RegionService;
import com.mpool.common.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping({ "/region", "/apis/region" })
@Api("区域管理")
public class RegionController {
	@Autowired
	private RegionService regionService;

	@GetMapping("/selectRegion")
	@ApiOperation("获取区域下拉菜单")
	public Result selectRegion() {
		return Result.ok(regionService.selectRegion());
	}
}
