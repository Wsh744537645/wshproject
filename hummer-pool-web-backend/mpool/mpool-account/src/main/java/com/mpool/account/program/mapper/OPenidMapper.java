package com.mpool.account.program.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.program.WxOpenid;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OPenidMapper extends BaseMapper<WxOpenid> {

  void deleteBinding(@Param("openid") String openid);
}
