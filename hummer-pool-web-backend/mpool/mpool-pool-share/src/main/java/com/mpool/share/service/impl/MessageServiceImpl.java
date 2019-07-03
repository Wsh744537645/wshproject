package com.mpool.share.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.share.mapper.MessageMapper;
import com.mpool.share.service.MessageService;
import com.mpool.common.model.account.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {
	@Autowired
	private MessageMapper messageMapper;

	@Override
	public Message findById(Integer id) {
		return messageMapper.selectById(id);
	}

	@Override
	public IPage<Message> list(IPage<Message> iPage, Message message) {
		return messageMapper.getList(iPage, message);
	}

}
