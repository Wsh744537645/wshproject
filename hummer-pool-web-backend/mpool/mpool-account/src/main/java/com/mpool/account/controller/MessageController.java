package com.mpool.account.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.account.service.MessageService;
import com.mpool.common.Result;
import com.mpool.common.model.account.Message;
import com.mpool.common.util.page.Page;
import com.mpool.common.util.page.PageModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping({ "/msg", "/apis/msg" })
public class MessageController {
	@Autowired
	private MessageService messageService;

	@GetMapping("/findByid")
	@ApiOperation("查看某条公告信息，参数示例: id=1")
	public Result findByid(@RequestParam(value = "id") Integer id) {
		Message findById = messageService.findById(id);
		return Result.ok(findById);
	}

	@GetMapping("/list")
	@ApiOperation("获取公告列表，参数示例: current=1&size=10")
	public Result list(PageModel pageModel, Message message) {
		IPage<Message> iPage = new Page<>(pageModel);
		iPage = messageService.list(iPage, message);
		return Result.ok(iPage);
	}
}
