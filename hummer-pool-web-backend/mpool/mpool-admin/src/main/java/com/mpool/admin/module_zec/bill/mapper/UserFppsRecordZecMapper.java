package com.mpool.admin.module_zec.bill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.admin.mapperUtils.bill.mapperBase.UserFppsRecordBaseMapper;
import com.mpool.admin.module_zec.bill.model.UserFppsRecordZec;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserFppsRecordZecMapper extends BaseMapper<UserFppsRecordZec>, UserFppsRecordBaseMapper {

}
