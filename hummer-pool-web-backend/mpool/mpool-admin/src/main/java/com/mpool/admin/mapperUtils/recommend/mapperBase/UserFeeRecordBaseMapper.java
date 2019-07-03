package com.mpool.admin.mapperUtils.recommend.mapperBase;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.UserFeeRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/14 16:37
 */
public interface UserFeeRecordBaseMapper<T> extends BaseMapper<T> {
    void insets(@Param("list") List<UserFeeRecord> list);
}
