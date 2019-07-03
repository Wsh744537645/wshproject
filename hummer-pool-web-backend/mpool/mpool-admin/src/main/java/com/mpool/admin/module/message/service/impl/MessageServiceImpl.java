package com.mpool.admin.module.message.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.module.message.mapper.MessageMapper;
import com.mpool.admin.module.message.service.MessageService;
import com.mpool.common.model.account.Message;

@Service
public class MessageServiceImpl implements MessageService {
	@Autowired
	private MessageMapper messageMapper;

	@Override
	public void release(Message message) {
		messageMapper.updateByRelease(message);
	}

	@Override
	public void add(Message message) {
		messageMapper.insert(message);
	}

	@Override
	public void edit(Message message) {
		messageMapper.updateById(message);
	}

	@Override
	public String show(Integer id) {
		Message selectById = messageMapper.selectById(id);
		return selectById.getText();
	}

	@Override
	public Message findById(Integer id) {
		return messageMapper.selectById(id);
	}

	@Override
	public IPage<Message> list(IPage<Message> iPage, Message message) {
		return messageMapper.getList(iPage, message);
	}

	@Override
	public void isEnable(Integer id, boolean b) {
		messageMapper.updateByEnable(id, b);
	}

	@Override
	public void delete(Integer id) {
		messageMapper.deleteById(id);
	}

}
