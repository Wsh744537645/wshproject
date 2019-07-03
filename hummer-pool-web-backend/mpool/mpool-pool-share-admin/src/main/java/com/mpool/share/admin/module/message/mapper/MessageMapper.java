package com.mpool.share.admin.module.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.account.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {

	IPage<Message> getList(IPage<Message> iPage, Message message);

	void updateByRelease(@Param("message") Message message);

	void updateByEnable(@Param("id") Integer id, @Param("enable") boolean b);

}
