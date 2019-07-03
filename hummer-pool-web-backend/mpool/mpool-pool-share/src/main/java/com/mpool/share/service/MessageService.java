package com.mpool.share.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.account.Message;

public interface MessageService {
	Message findById(Integer id);

	IPage<Message> list(IPage<Message> iPage, Message message);
}
