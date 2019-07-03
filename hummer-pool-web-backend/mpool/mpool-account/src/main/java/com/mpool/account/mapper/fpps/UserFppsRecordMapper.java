package com.mpool.account.mapper.fpps;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.account.fpps.UserFppsRecord;

@Mapper
public interface UserFppsRecordMapper extends BaseMapper<UserFppsRecord> {

	void inserts(@Param("list") List<UserFppsRecord> fppsRecords);

	/**
	 * 获得用户过去一天收益
	 * 
	 * @param yyyymMdd
	 * @param userIds
	 * @return
	 */
	List<UserFppsRecord> pastDayRecord(@Param("day") String yyyymMdd, @Param("list") List<Long> userIds);

	IPage<UserFppsRecord> getUserFppsRecordByPuid(IPage<UserFppsRecord> iPage, @Param("puid") Long puid);

}
