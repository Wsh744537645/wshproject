package com.mpool.share.admin.module.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.share.admin.InModel.OutUserLog;
import com.mpool.common.model.account.log.UserRegLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface UserRegLogMapper extends BaseMapper<UserRegLog> {

	IPage<Map<String, Object>> getAccountRegisterLog(IPage<UserRegLog> page,
                                                     @Param("userRegLog") UserRegLog userRegLog);

	/**
	 * 获取用户登录后的操作日志
	 */
	IPage<OutUserLog> getUserLog(IPage<OutUserLog> page, @Param("username") String username);

}
