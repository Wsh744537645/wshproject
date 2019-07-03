package com.mpool.admin.module.bill.mapper;

import com.mpool.admin.mapperUtils.bill.mapperBase.UserFppsRecordBaseMapper;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.fpps.UserFppsRecord;

@Mapper
public interface UserFppsRecordMapper extends BaseMapper<UserFppsRecord>, UserFppsRecordBaseMapper {

}
