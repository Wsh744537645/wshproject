package com.mpool.admin.module.message.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.account.Message;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {

	IPage<Message> getList(IPage<Message> iPage, Message message);

	void updateByRelease(@Param("message") Message message);

	void updateByEnable(@Param("id") Integer id, @Param("enable") boolean b);

}
