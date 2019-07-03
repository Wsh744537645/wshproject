package com.mpool.admin.module.account.mapper;

import java.util.List;
import java.util.Map;

import com.mpool.admin.module.account.model.UserPayBillOut;
import com.mpool.common.model.account.RecommendUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.account.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

	IPage<UserPayBillOut> masterListPage(IPage<User> page, @Param("user") User user, @Param("currencyId") Integer currencyId);
	List<UserPayBillOut> masterListObjectPage(@Param("userList") List<Long> userList, @Param("currencyId") Integer currencyId);

	List<Long> getUserIdByMasterUserId(@Param("userId") Long userId);

	IPage<Map<String, Object>> memberListPage(IPage<User> page, @Param("user") User user, @Param("currencyId") Integer currencyId);

	IPage<UserPayBillOut> memberListByObjectPage(IPage<User> page, @Param("user") User user, @Param("currencyId") Integer currencyId);

	/**
	 * wgg
	 * 根据用户名查询用户的信息（当前算力，过去一天算力，账号余额，昨日收益，总收益，累计转账...
	 * @param username
	 * @return
	 */
	List<UserPayBillOut> getUserListByUsername(@Param("username") String username, @Param("currencyId") Integer currencyId);
	List<UserPayBillOut> masterAllList( @Param("user") User user, @Param("currencyId") Integer currencyId);

	/**
	 * 查主用户信息
	 * @param username
	 * @return
	 */
	User getUserByname(@Param("username")String username);
	User getMasterUserByname(@Param("username")String username);
	User getUserInfo(@Param("username")String username);
	User getMasterUsername(@Param("masterId") Long masterId);
	/**
	 * 设置推荐人后更新user_Group状态
	 * @param userId
	 */
    void updateUserGroupById(@Param("userId") Long userId);
	void updateUserGroupByIdMember(@Param("userId") Long userId);
	/**
	 *wgg
	 * 获取主账号信息 通过推荐人搜索，子用户搜索，主用户搜索
	 * @param page
	 * @return
	 */
	IPage<User> getMasterUserInfo(IPage<User> page);
	IPage<User> getMasterUserInfos(IPage<User> page,@Param("id")Long id);
	IPage<User> getMasterUserInfoList(IPage<User> page,@Param("username")String username,@Param("recommendName")String recommendName);

	void updatePasswordByUsername(@Param("psw") String psw,@Param("username") String username);

	IPage<User> getUserInfos(IPage<User> page,@Param("username") String username);
	List<User> getUserBy(@Param("username") String username);

    User getUserInfoByUserId(@Param("userId") Long userId);
}
