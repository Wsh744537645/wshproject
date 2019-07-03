package com.mpool.admin.module.recommend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.admin.mapperUtils.recommend.mapperBase.UserFeeRecordBaseMapper;
import com.mpool.common.model.account.UserFeeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserFeeRecordMapper extends UserFeeRecordBaseMapper<UserFeeRecord> {

}
