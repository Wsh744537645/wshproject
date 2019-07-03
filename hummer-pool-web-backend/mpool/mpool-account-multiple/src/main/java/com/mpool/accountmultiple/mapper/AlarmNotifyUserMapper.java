package com.mpool.accountmultiple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.AlarmNotifyUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlarmNotifyUserMapper extends BaseMapper<AlarmNotifyUser> {
	/**
	 * wgg 获取告警列表
	 * 
	 * @param userId
	 * @return
	 */
	List<AlarmNotifyUser> settingUserList(Long userId);

	/**
	 * wgg 删除告警单条&批量
	 * 
	 * @param ids
	 */
	void deleteSettingUserIds(@Param("ids") List<Integer> ids);

	void insets(@Param("list") List<AlarmNotifyUser> notifyUser, @Param("userId") Long userId);

	List<AlarmNotifyUser> getNotifyUsers(@Param("userId") Long userId);

}
