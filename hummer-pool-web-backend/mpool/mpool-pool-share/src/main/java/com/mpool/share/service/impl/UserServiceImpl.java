package com.mpool.share.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.CoinValidationUtil;
import com.mpool.common.cache.service.EmailCodeCacheService;
import com.mpool.common.cache.service.PhoneCodeCahceService;
import com.mpool.common.model.account.IpRegion;
import com.mpool.common.model.share.UserInvite;
import com.mpool.common.model.share.UserPayModel;
import com.mpool.common.model.share.UserRecommend;
import com.mpool.share.config.shiro.token.JWTToken;
import com.mpool.share.mapper.UserNotifyMapper;
import com.mpool.share.mapper.UserPayMapper;
import com.mpool.share.mapper.recommend.UserInviteMapper;
import com.mpool.share.mapper.recommend.UserRecommendMapper;
import com.mpool.share.model.*;
import com.mpool.common.model.account.UserRegion;
import com.mpool.common.model.account.log.UserRegLog;
import com.mpool.common.model.log.LogUserOperation;
import com.mpool.common.util.*;
import com.mpool.share.exception.ExceptionCode;
import com.mpool.share.exception.ShareException;
import com.mpool.share.mapper.UserIpAddessMapper;
import com.mpool.share.mapper.UserMapper;
import com.mpool.share.mapper.log.UserRegLogMapper;
import com.mpool.share.service.*;
import com.mpool.share.service.log.UserOperationService;
import com.mpool.share.utils.AccountSecurityUtils;
import com.mpool.share.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.*;

import static com.mpool.share.constant.Constant.*;

/**
 * @Author: stephen
 * @Date: 2019/4/30 11:56
 */

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    /**
     * 用户表操作
     */
    @Autowired
    private UserMapper usersMapper;
    /**
     * 发送邮件服务
     */
    @Autowired
    private EmailService mailService;
    /**
     * 邮件缓存服务
     */
    @Autowired
    private EmailCodeCacheService emailCodeCacheService;
    /**
     * 用户角色服务
     */
    @Autowired
    private UserRoleService userRoleService;
    /**
     * 用户区域服务
     */
    @Autowired
    private UserRegionSerice userRegionSerice;
    /**
     * 发送短信服务
     */
    @Autowired
    private SmsService smsService;
    /**
     * 短信验证码缓存服务
     */
    @Autowired
    private PhoneCodeCahceService phoneCodeCahceService;

    @Autowired
    private UserRegLogMapper userRegLogMapper;

    @Autowired
    private UserOperationService userOperationService;

    /**
     * 每次登录确实是否为新ip，新ip需要获取地理位置保存数据库
     */
    @Autowired
    private UserIpAddessMapper userIpAddessMapper;
    @Autowired
    private UserPayMapper userPayMapper;
    @Autowired
    private UserNotifyMapper notifyMapper;
    @Autowired
    private UserRecommendMapper recommendMapper;
    @Autowired
    private UserInviteMapper inviteMapper;

    @Override
    public User findByUsername(String username) {
        return usersMapper.findByUsername(username);
    }

    @Override
    public User findByUserId(Long userId) {
        return usersMapper.findById(userId);
    }

    @Override
    @Transactional
    public int reg(User users, String recommendCode) {
        Date date = new Date();
        String password = users.getPassword();
        users.setCreateTime(date);
        users.setNickName(users.getUsername());
        users.setPassword(EncryUtil.encrypt(password));
        int insert = usersMapper.insert(users);
        userRoleService.saveUserRole(users.getUserId(), 1);

        UserRegion userRegion = new UserRegion();
        userRegion.setUserId(users.getUserId());
        userRegion.setRegionId(1);
        userRegionSerice.seva(userRegion);

        //创建钱包
        UserPayModel userPayModel = new UserPayModel();
        userPayModel.setPuid(users.getUserId());
        userPayModel.setCreateTime(new Date());
        userPayMapper.insert(userPayModel);

        //创建到期提醒
        UserNotify notify = new UserNotify();
        notify.setPuid(users.getUserId());
        notifyMapper.insert(notify);

        //保存推荐信息
        if(recommendCode != null && !recommendCode.isEmpty()){
            UserRecommend checkReCommend = new UserRecommend();
            checkReCommend.setCode(recommendCode);
            checkReCommend = recommendMapper.selectOne(new QueryWrapper<>(checkReCommend));
            if(checkReCommend != null){
                UserInvite invite = new UserInvite();
                invite.setRecommendUid(checkReCommend.getPuid());
                invite.setInviteUid(users.getUserId());
                invite.setRate(checkReCommend.getRate());
                invite.setCreateTime(new Date());
                inviteMapper.insert(invite);
            }else{
                log.error("【注册】邀请码不存在，code={}", recommendCode);
            }
        }

        //创建专属邀请码
        UserRecommend recommend = new UserRecommend();
        recommend.setCode(UUIDUtil.generateNumber(6));
        recommend.setPuid(users.getUserId());
        recommend.setCreateTime(new Date());
        recommendMapper.insert(recommend);

        //记录日志
        UserRegLog entity = new UserRegLog();
        entity.setCreateBy(0L);
        entity.setCreateTime(date);
        entity.setDesc("注册");
        entity.setRegIp(AccountSecurityUtils.getHost());
        entity.setUserId(users.getUserId());
        userRegLogMapper.insert(entity);
        LogUserOperation model = new LogUserOperation();
        model.setUserId(users.getUserId());
        model.setUserType("account");
        model.setLogType(1);
        model.setContent("注册主账户");
        model.setCreatedTime(date);
        userOperationService.insert(model);//插入log_user_operation表
        return insert;
    }

    @Override
    public String login(String username, String password, String telphoneCode) {
        Session session = AccountSecurityUtils.getSubject().getSession();
//        UsernamePasswordTelphoneToken token = new UsernamePasswordTelphoneToken(username, password, telphoneCode);
//        Subject subject = AccountSecurityUtils.getSubject();
//        //login 会 直接到UsernamePasswordRealm离认证方法设置的来验证
//        subject.login(token);

        String token = JWTUtils.sign(username, EncryUtil.encrypt(password));
        AccountSecurityUtils.getSubject().login(new JWTToken(token));

        User users = this.findByUsername(username);

        String ip = "";
        if(session != null) {
            session.getHost();
        }
        users.setLastIp(ip);
        users.setLastTime(new Date());
        usersMapper.updateById(users);
        IpRegion ipRegion = userIpAddessMapper.getIpAddess(ip);
        if (ipRegion == null){
            IpRegion model = new IpRegion();
            model.setUserId(users.getUserId());
            model.setCreatedTime(new Date());
            model.setIp(ip);
            String strIp = getCityInfoByIp(ip);
            if (strIp == null){
                model.setRegion("未知区域");
            }else{
                model.setRegion(strIp);
            }
            userIpAddessMapper.insert(model);
        }
        return token;
    }

    private static String getCityInfoByIp(String ip){
//		ip = "183.14.132.72";
        if (ip.equals("0:0:0:0:0:0:0:1")){
            return null;
        }
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> forEntity = restTemplate
                    .getForEntity("http://freeapi.ipip.net/{ip}", String.class,ip);
            String fromJson =forEntity.getBody();

            String countries = fromJson.substring(2,4);
            String province = fromJson.substring(7,9);
            String city = fromJson.substring(12,14);
            String name = countries+"|"+province+"|"+city;
            return name;
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    @Override
    public String sendCodeByEmail(String mail) {
        User users = usersMapper.findBymail(mail);
        if (users != null) {
            throw new ShareException(ExceptionCode.email_exists);
        }
        String code = UUIDUtil.generateNumber(6);
        String sessionId = AccountSecurityUtils.getSubject().getSession().getId().toString();
        String key = CODE_REG_PREFIX + emailCodeCacheService.builderKey(sessionId, code);
        emailCodeCacheService.putCode(key, code);
        try {
            mailService.sendSimple(mail, "注册验证码", code);
        } catch (Exception e) {
            log.debug("", e);
        }

        return code;
    }

    @Override
    public String resetPasswordCode(String mail) {
        User users = usersMapper.findBymail(mail);
        if (users == null) {
            throw new ShareException(ExceptionCode.email_not_find);
        }
        String code = UUIDUtil.generateNumber(6);
        String sessionId = AccountSecurityUtils.getSubject().getSession().getId().toString();
        String key = CODE_REST_PASSWORD_PREFIX + emailCodeCacheService.builderKey(sessionId, code);
        emailCodeCacheService.putCode(key, code);
        mailService.sendSimple(mail, "修改密码验证码", code);
        return code;
    }

    @Override
    public void resetPassword(User user) {
        User u = null;
        if (!StringUtils.isEmpty(user.getUserEmail())) {
            u = usersMapper.findBymail(user.getUserEmail());
            if (u == null) {
                throw new ShareException(ExceptionCode.email_not_find);
            }
        }
        if (!StringUtils.isEmpty(user.getUserPhone())) {
            u = usersMapper.findByPhone(user.getUserPhone());
            if (u == null) {
                throw new ShareException(ExceptionCode.phone_not_exists);
            }
        }
        if (u == null) {
            throw new ShareException(ExceptionCode.user_not_exists);
        }
        User userr = new User();
        userr.setPassword(EncryUtil.encrypt(user.getPassword()));
        userr.setUserId(u.getUserId());
        usersMapper.updateById(userr);
    }

    @Override
    public void checkExistByMail(String mail) {
        if (usersMapper.findBymail(mail) != null) {
            throw new ShareException(ExceptionCode.email_exists);
        }
    }

    @Override
    public void checkExistByUsername(String username) {
        if (usersMapper.findByUsername(username) != null) {
            throw new ShareException(ExceptionCode.username_exists);
        }
    }

    @Override
    public void checkExistByPhone(String phone) {
        if (usersMapper.findByPhone(phone) != null) {
            throw new ShareException(ExceptionCode.phone_exists);
        }
    }

    @Override
    public void insert(User entity) {
        usersMapper.insert(entity);
    }

    @Override
    public void inserts(List<User> entitys) {
        for (User user : entitys) {
            insert(user);
        }
    }

    @Override
    public void update(User entity) {
        usersMapper.updateById(entity);
    }

    @Override
    public void delete(Serializable id) {
        usersMapper.deleteById(id);
    }

    @Override
    public User findById(Serializable id) {
        return usersMapper.selectById(id);
    }

    @Override
    public void updatePassword(String username, String oldPassword, String newPassword) {
        User user = usersMapper.findByUsername(username);
        if (user.getPassword().equals(EncryUtil.encrypt(oldPassword))) {
            user.setPassword(EncryUtil.encrypt(newPassword));
            usersMapper.updateById(user);
        } else {
            throw new ShareException(ExceptionCode.password_error);
        }
        SecurityUtils.getSubject().logout();
    }

    @Override
    public void updateEmail(String username, String email) {
        User user = usersMapper.findByUsername(username);
        user.setUserEmail(email);
        usersMapper.updateById(user);
        User user2 = AccountSecurityUtils.getUser();
        user2.setUserEmail(email);
    }

    @Override
    public String sendLoginSmsCode(String username, String phoneNumber) {
        String code = UUIDUtil.generateNumber(6);
        String key = RedisKeys.getLoginSmsCode(username, phoneNumber);
        phoneCodeCahceService.putCode(key, code);
        smsService.sendSmsCode(phoneNumber, code, phoneNumber, null);
        return code;
    }

    @Override
    public String sentRegSmsCode(String phoneNumber) {
        User user = usersMapper.findByPhone(phoneNumber);
        if (user != null) {
            throw new ShareException(ExceptionCode.phone_exists);
        }
        String code = UUIDUtil.generateNumber(6);
        String sessionId = SecurityUtils.getSubject().getSession().getId().toString();
        String key = phoneCodeCahceService.builderKey(sessionId, code);
        phoneCodeCahceService.putCode(key, code);
        log.warn("{} sms code is, code={}", phoneNumber, code);
        try {
            smsService.sendSmsCode(phoneNumber, code, phoneNumber, null);
        } catch (Exception e) {
            log.error("", e);
        }

        return code;
    }

    @Override
    public String sentRestPasswordSmsCode(String phoneNumber) {
        User user = usersMapper.findByPhone(phoneNumber);
        if (user == null) {
            throw new ShareException(ExceptionCode.phone_not_exists);
        }
        String outId = phoneNumber;
        String code = UUIDUtil.generateNumber(6);
        String sessionId = SecurityUtils.getSubject().getSession().getId().toString();
        String key = phoneCodeCahceService.builderKey(sessionId, code);
        phoneCodeCahceService.putCode(key, code);
        try {
            smsService.sendSmsCode(phoneNumber, code, outId, null);
        } catch (Exception e) {
            log.error("", e);
        }
        return code;
    }

    @Override
    public void updatePhone(String username, String phone) {
        User user = usersMapper.findByUsername(username);
        user.setUserPhone(phone);
        usersMapper.updateById(user);
        User user2 = AccountSecurityUtils.getUser();
        user2.setUserPhone(phone);
    }

    @Override
    public void updateUserInfo(UserInfo userInfo) {
        String password = userInfo.getPassword();
        String username = userInfo.getUsername();
        User user = usersMapper.findByUsername(username);
        if (user.getPassword().equals(EncryUtil.encrypt(password))) {
            if (userInfo.getEmail() != null) {
                user.setUserEmail(userInfo.getEmail());
            }
            if (userInfo.getPhone() != null) {
                user.setUserPhone(userInfo.getPhone());
            }
            usersMapper.updateById(user);
        } else {
            throw new ShareException(ExceptionCode.password_error);
        }
        User userCurr = AccountSecurityUtils.getUser();
        if (userInfo.getEmail() != null) {
            userCurr.setUserEmail(userInfo.getEmail());
        }
        if (userInfo.getPhone() != null) {
            userCurr.setUserPhone(userInfo.getPhone());
        }
    }

    @Override
    public String sendCodeByPhone(String phone) {

        User user = usersMapper.findByPhone(phone);
        if (user != null) {
            throw new ShareException(ExceptionCode.phone_exists);
        }
        String code = UUIDUtil.generateNumber(6);
        String sessionId = AccountSecurityUtils.getSubject().getSession().getId().toString();
        String key = phoneCodeCahceService.builderKey(sessionId, code);
        phoneCodeCahceService.putCode(key, code);
        try {
            smsService.sendSmsCode(phone, code, phone, null);
        } catch (Exception e) {
            log.error("", e);
        }
        return code;
    }

    @Override
    public IPage<User> selectUserPage(IPage<User> iPage, User user) {
        return usersMapper.selectPage(iPage, new QueryWrapper<User>(user));
    }

    @Override
    public User getCurrentUser() {
        User user = AccountSecurityUtils.getUser();
        user.setPassword(null);

        return user;
    }

    @Override
    public String sendSecurityCodeEmail(String email) {
        String code = UUIDUtil.generateNumber(6);
        String sessionId = AccountSecurityUtils.getSubject().getSession().getId().toString();
        String key = emailCodeCacheService.builderKey(sessionId, code);
        emailCodeCacheService.putCode(key, code);
        mailService.sendSimple(email, "安全验证码", code);
        return code;
    }

    @Override
    public String sendSecurityCodePhone(String phone) {
        String code = UUIDUtil.generateNumber(6);
        String sessionId = AccountSecurityUtils.getSubject().getSession().getId().toString();
        String key = phoneCodeCahceService.builderKey(sessionId, code);
        phoneCodeCahceService.putCode(key, code);
        try {
            smsService.sendSmsCode(phone, code, phone, null);
        } catch (Exception e) {
            log.error("", e);
        }
        return code;
    }

    @Override
    public void sendCodeByResetMail(String mail) {
        String code = UUIDUtil.generateNumber(6);
        try {
            String sessionId = AccountSecurityUtils.getSubject().getSession().getId().toString();
            String key = CODE_MAIL_SETTING_PREFIX + emailCodeCacheService.builderKey(sessionId, code);
            emailCodeCacheService.putCode(key, code);
            mailService.sendSimple(mail, "修改邮箱验证码", code);
        } catch (Exception e) {
            log.debug("", e);
        }
    }

    @Override
    public void updateMailByUserId(Long userId, String mail) {
        User user = usersMapper.findById(userId);
        user.setUserEmail(mail);
        usersMapper.updateById(user);
        User user2 = AccountSecurityUtils.getUser();
        user2.setUserEmail(mail);
    }

    @Override
    public void sendCodeByRestPhone(String phone) {
        String code = UUIDUtil.generateNumber(6);
        String sessionId = AccountSecurityUtils.getSubject().getSession().getId().toString();
        String key = CODE_PHONE_SETTING_PREFIX + phoneCodeCahceService.builderKey(sessionId, code);
        phoneCodeCahceService.putCode(key, code);
        smsService.sendSmsCode(phone, code, phone, "SMS_166777758");
    }

    @Override
    public void updatePhoneByUserId(Long userId, String phone) {
        User user = usersMapper.findById(userId);
        user.setUserPhone(phone);
        usersMapper.updateById(user);
        User user2 = AccountSecurityUtils.getUser();
        user2.setUserPhone(phone);
    }

    @Override
    public void sendPhoneCodeByWalletAddress(String phone) {
        String code = UUIDUtil.generateNumber(6);
        String sessionId = AccountSecurityUtils.getSubject().getSession().getId().toString();
        String key = CODE_PHONE_WALLET_PREFIX + phoneCodeCahceService.builderKey(sessionId, code);
        phoneCodeCahceService.putCode(key, code);
        smsService.sendSmsCode(phone, code, phone, "SMS_166777758");
    }

    @Override
    public void sendMailCodeByWalletAddress(String mail) {
        String code = UUIDUtil.generateNumber(6);
        try {
            String sessionId = AccountSecurityUtils.getSubject().getSession().getId().toString();
            String key = CODE_MAIL_WALLET_PREFIX + emailCodeCacheService.builderKey(sessionId, code);
            emailCodeCacheService.putCode(key, code);
            mailService.sendSimple(mail, "动态验证码", code);
        } catch (Exception e) {
            log.debug("", e);
        }
    }

    @Override
    public void updateWalletAddress(Long userId, String walletAddress) {
        boolean bitCoinAddressValidate = CoinValidationUtil.bitCoinAddressValidate(walletAddress);
        if(!bitCoinAddressValidate){
            throw new ShareException(ExceptionCode.wallet_address_error);
        }

        UserPayModel model = new UserPayModel();
        model.setPuid(userId);
        model.setWalletAddress(walletAddress);
        UserPayModel model1 = new UserPayModel();
        model1.setPuid(userId);
        userPayMapper.update(model, new QueryWrapper<>(model1));
    }

    @Override
    public void sendPhoneCodeByPassward(String phone) {
        String code = UUIDUtil.generateNumber(6);
        String sessionId = AccountSecurityUtils.getSubject().getSession().getId().toString();
        String key = CODE_PHONE_PASSWORD_PREFIX + phoneCodeCahceService.builderKey(sessionId, code);
        phoneCodeCahceService.putCode(key, code);
        smsService.sendSmsCode(phone, code, phone, "SMS_166777758");
    }

    @Override
    public void sendMailCodeByPassward(String mail) {
        String code = UUIDUtil.generateNumber(6);
        try {
            String sessionId = AccountSecurityUtils.getSubject().getSession().getId().toString();
            String key = CODE_MAIL_PASSWORD_PREFIX + emailCodeCacheService.builderKey(sessionId, code);
            emailCodeCacheService.putCode(key, code);
            mailService.sendSimple(mail, "动态验证码", code);
        } catch (Exception e) {
            log.debug("", e);
        }
    }

    @Override
    public void updatePassward(Long userId, String passward) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decode = decoder.decode(passward);
        String pa = new String(decode);
        User user = new User();
        user.setUserId(userId);
        user.setPassword(EncryUtil.encrypt(pa));
        usersMapper.updateById(user);
    }
}
