package com.mpool.share.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.share.model.User;
import com.mpool.share.model.UserInfo;

/**
 * @Author: stephen
 * @Date: 2019/4/30 11:33
 */
public interface UserService extends BaseService<User> {

    User findByUsername(String username);

    User findByUserId(Long userId);

    int reg(User users, String recommendCode);

    String login(String username, String password, String telphoneCode);

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
     * 修改邮箱 发送验证码
     * @param mail
     */
    void sendCodeByResetMail(String mail);
    void updateMailByUserId(Long userId, String mail);

    /**
     * 修改手机号 发送验证码
     * @param phone
     */
    void sendCodeByRestPhone(String phone);
    void updatePhoneByUserId(Long userId, String phone);

    /**
     * 手机验证码修改用户收款钱包地址
     * @param phone
     */
    void sendPhoneCodeByWalletAddress(String phone);
    void sendMailCodeByWalletAddress(String mail);
    void updateWalletAddress(Long userId, String walletAddress);

    /**
     * 手机验证码修改用户密码
     * @param phone
     */
    void sendPhoneCodeByPassward(String phone);
    void sendMailCodeByPassward(String mail);
    void updatePassward(Long userId, String passward);

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

    String sendSecurityCodeEmail(String userEmail);

    String sendSecurityCodePhone(String userEmail);
}
