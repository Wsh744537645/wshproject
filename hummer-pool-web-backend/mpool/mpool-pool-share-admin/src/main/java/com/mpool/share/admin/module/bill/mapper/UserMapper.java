package com.mpool.share.admin.module.bill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.share.admin.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @Author: stephen
 * @Date: 2019/4/30 11:56
 */


@Mapper
public interface UserMapper extends BaseMapper<User> {

    User findByUsername(@Param("username") String username);

    User findById(@Param("userId") Long userId);

    IPage<Map<String, Object>> getUsersInfoList(IPage<Map<String, Object>> page, @Param("username") String username);
}
