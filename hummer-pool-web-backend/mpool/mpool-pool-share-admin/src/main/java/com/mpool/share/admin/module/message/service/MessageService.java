package com.mpool.share.admin.module.message.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.account.Message;

public interface MessageService {
	/**
	 * @param id
	 * @param date date == null 代表撤回
	 */
	void release(Message message);

	void isEnable(Integer id, boolean b);

	void add(Message message);

	void edit(Message message);

	String show(Integer id);

	Message findById(Integer id);

	IPage<Message> list(IPage<Message> iPage, Message message);

	void delete(Integer id);
}
