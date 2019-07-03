package com.mpool.admin.mapperUtils.bill.mapperBase;

import com.mpool.common.model.account.fpps.UserFppsRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/13 20:22
 */
public interface UserFppsRecordBaseMapper {
    List<UserFppsRecord> masterUserFppsRecord(@Param("userList") List<Long> userList, @Param("day") String day);
}
