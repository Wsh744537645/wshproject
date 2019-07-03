package com.mpool.share.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.IpRegion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserIpAddessMapper extends BaseMapper<IpRegion> {

	IpRegion getIpAddess(@Param("ip") String ip);
}
