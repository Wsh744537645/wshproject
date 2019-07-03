package com.mpool.share.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.share.model.NotifyResultModel;
import com.mpool.share.model.UserNotify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/31 9:52
 */

@Mapper
public interface UserNotifyMapper extends BaseMapper<UserNotify> {

    List<NotifyResultModel> getNotifyByUserIds(@Param("ids") List<Long> ids);
}
