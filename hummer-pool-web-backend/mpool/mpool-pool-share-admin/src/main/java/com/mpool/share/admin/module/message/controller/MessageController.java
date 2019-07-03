package com.mpool.share.admin.module.message.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.share.admin.module.message.service.MessageService;
import com.mpool.share.admin.utils.AdminSecurityUtils;
import com.mpool.common.Result;
import com.mpool.common.model.account.Message;
import com.mpool.common.util.page.Page;
import com.mpool.common.util.page.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping({ "/msg", "/apis/msg" })
public class MessageController {
	@Autowired
	private MessageService messageService;

	@PostMapping("/add")
	public Result add(@RequestBody Message message) {
		Long userId = AdminSecurityUtils.getUser().getUserId();
		if (message.getReleaseTime() != null) {
			message.setReleaseBy(userId);
		}

		if (message.getId() != null) {
			message.setUpdateBy(userId);
			message.setUpdateTime(new Date());
			messageService.edit(message);
		} else {
			message.setCreateTime(new Date());
			message.setCreateBy(userId);
			messageService.add(message);
		}
		return Result.ok();
	}

	@PostMapping("/edit")
	public Result edit(@RequestBody Message message) {
		Long userId = AdminSecurityUtils.getUser().getUserId();
		message.setUpdateBy(userId);
		message.setUpdateTime(new Date());
		messageService.edit(message);
		return Result.ok();
	}

	@GetMapping("/findByid")
	public Result findByid(@RequestParam(value = "id") Integer id) {
		Message findById = messageService.findById(id);
		return Result.ok(findById);
	}

	@GetMapping("/show")
	public Result show(@RequestParam(value = "id") Integer id) {
		String txt = messageService.show(id);
		return Result.ok(txt);
	}

	@GetMapping("/list")
	public Result list(PageModel pageModel, Message message) {
		IPage<Message> iPage = new Page<>(pageModel);
		iPage = messageService.list(iPage, message);
		return Result.ok(iPage);
	}

	@PostMapping("/release")
	public Result release(@RequestBody Message message) {
		Long userId = AdminSecurityUtils.getUser().getUserId();
		message.setReleaseBy(userId);
		messageService.release(message);
		return Result.ok();
	}

	@GetMapping("/enable")
	public Result release(@RequestParam("id") Integer id, @RequestParam("enable") boolean b) {
		messageService.isEnable(id, b);
		return Result.ok();
	}

	@DeleteMapping("/delete")
	public Result release(@RequestParam("id") Integer id) {
		messageService.delete(id);
		return Result.ok();
	}

}
