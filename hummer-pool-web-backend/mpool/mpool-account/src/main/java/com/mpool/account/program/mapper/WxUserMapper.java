package com.mpool.account.program.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.User;
import com.mpool.common.model.account.Worker;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WxUserMapper extends BaseMapper<User> {
	/**
	 * wgg
	 * 微信小程序
	 * 根据OpenID获取用户信息
	 * @param openid
	 * @return
	 */
	User getUserInfoByOpenid(String openid);

}
