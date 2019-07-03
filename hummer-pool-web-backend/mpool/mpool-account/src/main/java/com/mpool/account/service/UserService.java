package com.mpool.account.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.account.model.UserInfo;
import com.mpool.account.model.subaccount.SubAccountList;
import com.mpool.common.model.account.User;
import com.mpool.common.model.account.UserRegion;
import com.mpool.common.model.account.bill.UserPay;

public interface UserService extends BaseService<User> {

	User findByUsername(String username);

	int reg(User users);

	void login(String username, String password, String telphoneCode);

	String sendCodeByEmail(String mail);

	String resetPasswordCode(String mail);

	void resetPassword(User user);

	/**
	 * @param mail 邮箱 如果邮箱存在则抛出 EpoolException
	 */
	void checkExistByMail(String mail);

	/**
	 * @param username 如果用户名存在则抛出 EpoolException
	 */
	void checkExistByUsername(String username);

	/**
	 * @param phone 如果手机号码存在则抛出 EpoolException
	 */
	void checkExistByPhone(String phone);

	/**
	 * 查询子账号
	 * 
	 * @param userId 主账号ID
	 * @return
	 */
	List<User> findSubAccount(String userId);
	List<User> findSubAccountByCurrencyId(Long userId);

	IPage<SubAccountList> findSubAccountInfo(IPage<SubAccountList> page, User user, String currencyname);

	/**
	 * 切换子账号
	 * 
	 * @param userId 被切换的userId
	 */
	User switchSubAccount(Long userId);

	/**
	 * 修改用户密码
	 * 
	 * @param username
	 * @param oldPassword
	 * @param newPassword
	 */
	void updatePassword(String username, String oldPassword, String newPassword);

	/**
	 * 修改邮箱
	 * 
	 * @param username
	 * @param password
	 * @param email
	 */
	void updateEmail(String username, String email);

	/**
	 * 发送手机短信验证码
	 * 
	 * @param phoneNumber
	 * @return
	 */
	String sendLoginSmsCode(String username, String phoneNumber);

	/**
	 * 注册发送短信验证码
	 * 
	 * @param phoneNumber
	 * @return
	 */
	String sentRegSmsCode(String phoneNumber);

	/**
	 * 忘记密码发送短信
	 * 
	 * @param phoneNumber
	 * @return
	 */
	String sentRestPasswordSmsCode(String phoneNumber);

	/**
	 * 修改手机
	 * 
	 * @param username 用户名
	 * @param password 密码
	 * @param phone    手机号码
	 */
	void updatePhone(String username, String phone);

	/**
	 * 修改手机或者邮箱
	 * 
	 * @param userInfo
	 */
	void updateUserInfo(UserInfo userInfo);

	/**
	 * 修改手机号码 发送收验证码
	 * 
	 * @param phone
	 * @return
	 */
	String sendCodeByPhone(String phone);

	/**
	 * 创建子账号
	 * 
	 * @param users      用户
	 * @param userWallet 钱包
	 * @param userRegion 区域
	 */
	void createSubAccount(User users, UserPay userPay, UserRegion userRegion);

	/**
	 * userPage
	 * 
	 * @param iPage
	 * @param user
	 * @return
	 */
	IPage<User> selectUserPage(IPage<User> iPage, User user);

	/**
	 * 获得当前登录用户信息
	 */
	User getCurrentUser();

	/**
	 * 主账号修改子账号的密码
	 * 
	 * @param userId
	 * @param newPassword
	 */
	void updateSubUserPassword(Long userId, String newPassword);

	String sendSecurityCodeEmail(String userEmail);

	String sendSecurityCodePhone(String userEmail);

	/**
	 * 根据钱包币种id获得用户列表
	 * @param iPage
	 * @param user
	 * @return
	 */
	IPage<User> selectUserPageByPayCurrencyId(IPage<User> iPage, Long masterId);

}
