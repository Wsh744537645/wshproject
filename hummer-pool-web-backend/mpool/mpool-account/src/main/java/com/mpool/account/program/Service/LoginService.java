package com.mpool.account.program.Service;

import com.mpool.common.Result;
import com.mpool.common.model.account.User;

import java.util.Map;

/**
 * 小程序登录接口
 * 
 * @author wgg
 *
 */
public interface LoginService {

	/**
	 * wgg
	 * 微信小程序登录接口
	 * @param username
	 * @param password
	 * @return token（60min有效期）
 	 */
	String loginWeixin(String username, String password);

	/**
	 * wgg
	 * 通过token获取用户信息
	 * @param token
	 * @return
	 */
	User getUserInfoByToken(String token);

	/**
	 * wgg
	 * 小程序绑定用户
	 * @param
	 */
	Map  bindingUser(String username, String password, String openid);
	/**
	 * wgg
	 * 小程序解绑用户
	 * @param username password
	 */
	Map deleteBinding(String username, String password, String openid);

	/**
	 * wgg
	 * 通过openid获取用户信息进行绑定主账号user，先检查是否已绑定，未绑定的话输入用户名密码进行绑定（把openid存入user表），已绑定了直接获取sessionid进行返回前端
	 * @param openid
	 */
	Map wxLogin(String openid);

	/**
	 * sessionID过期后需要重新用账号密码登录
	 * @param username
	 * @param password
	 * @return
	 */
	String reWxLogin(String username, String password);

}
