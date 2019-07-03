package com.mpool.accountmultiple.mapper.recommend;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.UserFeeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserFeeRecordMapper extends BaseMapper<UserFeeRecord> {

        void insets(@Param("list") List<UserFeeRecord> list);
}
