package com.mpool.admin.module.log.mapper;

import java.util.Map;

import com.mpool.admin.InModel.OutUserLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.account.log.UserRegLog;

@Mapper
public interface UserRegLogMapper extends BaseMapper<UserRegLog> {

	IPage<Map<String, Object>> getAccountRegisterLog(IPage<UserRegLog> page,
			@Param("userRegLog") UserRegLog userRegLog);

	/**
	 * 获取用户登录后的操作日志
	 */
	IPage<OutUserLog> getUserLog(IPage<OutUserLog> page, @Param("username") String username);

}
