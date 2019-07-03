package com.mpool.share.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.share.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: stephen
 * @Date: 2019/4/30 11:56
 */


@Mapper
public interface UserMapper extends BaseMapper<User> {

    User findByUsername(@Param("username") String username);

    User findBymail(@Param("mail") String mail);

    User findByPhone(@Param("phone") String phone);


    User findById(@Param("userId")Long userId);
}
